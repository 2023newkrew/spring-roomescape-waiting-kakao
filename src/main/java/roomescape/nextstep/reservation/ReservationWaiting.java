package roomescape.nextstep.reservation;

import lombok.Getter;

@Getter
public class ReservationWaiting extends Reservation {

    private final Long waitNum;

    public ReservationWaiting(Reservation reservation, Long waitNum) {
        super(reservation.getId(), reservation.getSchedule(), reservation.getMember(), reservation.getStatus());
        this.waitNum = waitNum;
    }
}
