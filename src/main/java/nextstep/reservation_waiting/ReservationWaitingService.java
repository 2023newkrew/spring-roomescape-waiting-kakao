package nextstep.reservation_waiting;

import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationWaitingService {
    private final ReservationWaitingDao reservationWaitingDao;

    public Long create(Reservation reservation, Member member) {
        return reservationWaitingDao.save(reservation, member);
    }
}
