package nextstep.reservation_waiting;

import auth.AuthenticationException;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationWaitingService {
    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new IllegalArgumentException("예약대기가 존재하지 않습니다.");
        }
        if (!reservation.sameMember(member)) {
            throw new IllegalArgumentException("예약을 한 당사자만이 지울 수 있습니다.");
        }

        reservationDao.deleteById(id);
    }
}
