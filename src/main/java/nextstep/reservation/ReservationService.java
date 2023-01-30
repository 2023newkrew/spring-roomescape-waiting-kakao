package nextstep.reservation;

import auth.AuthenticationException;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberDaoImpl;
import nextstep.reservation_waiting.ReservationWaiting;
import nextstep.reservation_waiting.ReservationWaitingDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.support.NonExistEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationWaitingDao reservationWaitingDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final MemberDaoImpl memberDao;

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NonExistEntityException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NonExistEntityException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NonExistEntityException();
        }
        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }
        List<ReservationWaiting> reservationWaitings = reservationWaitingDao.findAllByScheduleIdOrderByDesc(reservation.getSchedule().getId());
        if (!reservationWaitings.isEmpty()) {
            ReservationWaiting reservationWaiting = reservationWaitings.get(reservationWaitings.size() - 1);
            Reservation toSave = Reservation.fromReservationWaiting(reservationWaiting);
            reservationDao.save(toSave);
            reservationWaitingDao.deleteById(reservationWaiting.getId());
        }

        reservationDao.deleteById(id);
    }

    public List<Reservation> findAllByMember(Member member) {
        return reservationDao.findAllByMember(member);
    }
}
