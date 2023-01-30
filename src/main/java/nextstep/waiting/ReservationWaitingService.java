package nextstep.waiting;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ReservationWaitingService {
    public final ReservationWaitingDao reservationWaitingDao;
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.reservationDao = reservationDao;
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

        List<Reservation> reservations = new ArrayList<>(
                reservationWaitingDao.findByScheduleId(schedule.getId())
                        .stream()
                        .map(ReservationWaiting::getReservation)
                        .toList());

        reservations.addAll(reservationDao.findByScheduleId(schedule.getId()));

        reservations.stream()
                .filter(v -> v.sameMember(member))
                .findAny()
                .ifPresent(v -> {
                    throw new DuplicateEntityException();
                });

        Long waitingSeq = (long) reservations.size();

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
