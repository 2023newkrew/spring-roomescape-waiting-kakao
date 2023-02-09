package nextstep.reservation.event;

import nextstep.reservation.domain.Reservation;

public class ReservationApproveEvent {

    private final Reservation reservation;

    public ReservationApproveEvent(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
