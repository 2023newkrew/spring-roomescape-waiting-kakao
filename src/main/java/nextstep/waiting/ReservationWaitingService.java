package nextstep.waiting;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ReservationWaitingService {
    public final ReservationWaitingDao reservationWaitingDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long create(Member member, ReservationWaitingRequest reservationWaitingRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<ReservationWaiting> reservations = reservationWaitingDao.findByScheduleId(schedule.getId());
        reservations.stream()
                .filter(v -> v.getReservation().sameMember(member))
                .findAny()
                .ifPresent(v -> {
                    throw new DuplicateEntityException();
                });

        Long waitingSeq = reservations.stream()
                .max(Comparator.comparingLong(ReservationWaiting::getWaitingSeq))
                .map(ReservationWaiting::getWaitingSeq)
                .orElse(0L);

        ReservationWaiting newReservationWaiting = new ReservationWaiting(
                new Reservation(
                        schedule,
                        member),
                waitingSeq
        );

        return reservationWaitingDao.save(newReservationWaiting);
    }

    public List<ReservationWaiting> findByMemberId(Member member) {
        return reservationWaitingDao.findByMemberId(member.getId());
    }

    public void deleteById(Member member, Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findById(id);
        if (reservationWaiting == null) {
            throw new NullPointerException();
        }
        if (reservationWaiting.getWaitingSeq() == 0) {
            throw new NoSuchElementException();
        }

        if (!reservationWaiting.getReservation().sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationWaitingDao.deleteById(id);
        updateSeq(reservationWaiting.getReservation().getSchedule().getId(), reservationWaiting.getWaitingSeq());
    }

    private void updateSeq(Long scheduleId, Long seq) {
        List<ReservationWaiting> reservationWaitings = reservationWaitingDao.findByScheduleId(scheduleId);

        reservationWaitings = reservationWaitings.stream()
                .filter(v -> v.getWaitingSeq() > seq)
                .collect(Collectors.toList());

        reservationWaitings.forEach(ReservationWaiting::decreaseWaitingSeq);
        reservationWaitings.forEach(reservationWaitingDao::updateWaitingSeq);
    }
}
