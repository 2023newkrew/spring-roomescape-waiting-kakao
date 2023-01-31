package nextstep.reservation;

import static nextstep.exception.ErrorMessage.CANNOT_MAKE_RESERVATION_WAITING;
import static nextstep.exception.ErrorMessage.DUPLICATED_RESERVATION;
import static nextstep.exception.ErrorMessage.NOT_AUTHORIZED;
import static nextstep.exception.ErrorMessage.NOT_EXIST_RESERVATION;
import static nextstep.exception.ErrorMessage.NOT_EXIST_SCHEDULE;
import static nextstep.exception.ErrorMessage.NOT_EXIST_THEME;
import static nextstep.exception.ErrorMessage.NOT_OWN_RESERVATION;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nextstep.exception.AlreadyReservedScheduleException;
import nextstep.exception.AuthenticationException;
import nextstep.exception.DuplicateEntityException;
import nextstep.exception.NoReservationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException(NOT_AUTHORIZED.getMessage());
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException(NOT_EXIST_SCHEDULE.getMessage());
        }

        reservationDao.findByScheduleId(schedule.getId())
            .ifPresent(reservation -> {
                throw new DuplicateEntityException(DUPLICATED_RESERVATION.getMessage());
            });

        Reservation newReservation = new Reservation(
            schedule,
            member
        );

        return reservationDao.save(newReservation);
    }

    public Optional<Reservation> findByScheduleId(Long scheduleId) {
        return reservationDao.findByScheduleId(scheduleId);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        themeDao.findById(themeId)
            .orElseThrow(() -> new NullPointerException(NOT_EXIST_THEME.getMessage()));

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Reservation reservation, Member member) {
        if (!reservation.sameMember(member)) {
            throw new AuthenticationException(NOT_OWN_RESERVATION.getMessage());
        }

        reservationDao.deleteById(reservation.getId());
    }

    public void validateReservationOwner(Reservation reservation, Member member) {
        if (reservation.sameMember(member)) {
            throw new AlreadyReservedScheduleException(CANNOT_MAKE_RESERVATION_WAITING.getMessage());
        }
    }

    public Reservation findById(Long id) {
        return reservationDao.findById(id)
            .orElseThrow(() -> new NoReservationException(NOT_EXIST_RESERVATION.getMessage()));
    }

    public List<Reservation> findOwn(Member member) {
        return reservationDao.findAllByMemberId(member.getId());
    }
}
