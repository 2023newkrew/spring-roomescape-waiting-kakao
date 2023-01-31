package nextstep.reservation_waiting;

import auth.AuthenticationException;
import auth.UserDetail;
import lombok.RequiredArgsConstructor;
import nextstep.member.MemberService;
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
    private final MemberService memberService;

    public Long create(UserDetail userDetail, ReservationRequest reservationRequest) {
        if (userDetail == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                memberService.toMember(userDetail)
        );

        return reservationDao.save(newReservation);
    }

    public void deleteById(UserDetail userDetail, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }
        if (userDetail == null || !reservation.getMember().getId().equals(userDetail.getId())) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public List<ReservationWaitingResponse> findAllByMember(UserDetail userDetail) {
        return reservationDao.findAllWaitingByMemberId(userDetail.getId())
                .stream()
                .map(ReservationWaitingResponse::new)
                .toList();
    }
}
