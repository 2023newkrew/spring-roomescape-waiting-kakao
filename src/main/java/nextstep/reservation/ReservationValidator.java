package nextstep.reservation;

import auth.AuthenticationException;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.support.DuplicateEntityException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationValidator {
    private final ReservationDao reservationDao;

    public void validateCreation(Reservation reservation) {
        validateMember(reservation.getMember());
        validateSchedule(reservation.getSchedule());
        validateReservationDuplication(reservation.getSchedule());
    }

    public void validateDelete(Member member, Reservation reservation) {
        validateMember(member);
        validateReservation(reservation);
        validateToHave(member, reservation);
    }

    private void validateMember(Member member) {
        if (member == null) {
            throw new AuthenticationException();
        }
    }

    private void validateSchedule(Schedule schedule) {
        if (schedule == null) {
            throw new NullPointerException();
        }
    }

    private void validateReservationDuplication(Schedule schedule) {
        List<Reservation> reservationList = reservationDao.findByScheduleId(schedule.getId());
        if (!reservationList.isEmpty()) {
            throw new DuplicateEntityException();
        }
    }

    private void validateReservation(Reservation reservation) {
        if (reservation == null) {
            throw new NullPointerException();
        }
    }

    private void validateToHave(Member member, Reservation reservation) {
        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }
    }
}
