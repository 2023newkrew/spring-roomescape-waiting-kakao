package nextstep.reservation_waiting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.reservation.Reservation;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationWaiting {
    private Reservation reservation;
    private int waitNum;
}
