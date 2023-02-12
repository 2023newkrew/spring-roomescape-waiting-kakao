package nextstep.reservation.event;

import nextstep.reservation.domain.Reservation;

public class ReservationApproveCancelEvent {

    private final Reservation reservation;

    public ReservationApproveCancelEvent(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
