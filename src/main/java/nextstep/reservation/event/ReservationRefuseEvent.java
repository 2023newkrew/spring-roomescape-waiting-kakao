package nextstep.reservation.event;

import nextstep.reservation.domain.Reservation;

public class ReservationRefuseEvent {

    private final Reservation reservation;

    public ReservationRefuseEvent(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
