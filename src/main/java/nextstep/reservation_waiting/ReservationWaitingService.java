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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationWaitingService {
    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId()).orElseThrow(NullPointerException::new);
        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id).orElseThrow(NullPointerException::new);
        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }
        reservationDao.deleteById(id);
    }

    public List<ReservationWaitingResponse> findAllByMember(Member member) {
        return reservationDao.findAllWaitingByMemberId(member.getId())
                .stream()
                .map(ReservationWaitingResponse::new)
                .toList();
    }
}
