package nextstep.waiting;

import nextstep.reservation.Reservation;

public class CreateWaitingResponse {
    private final Waiting waiting;
    private final Reservation reservation;

    private CreateWaitingResponse(Waiting waiting, Reservation reservation) {
        this.waiting = waiting;
        this.reservation = reservation;
    }

    public static CreateWaitingResponse fromWaiting(Waiting waiting) {
        return new CreateWaitingResponse(waiting, null);
    }

    public static CreateWaitingResponse fromReservation(Reservation reservation) {
        return new CreateWaitingResponse(null, reservation);
    }

    public boolean isReservedDirectly() {
        return this.reservation != null;
    }


    public Waiting getWaiting() {
        return this.waiting;
    }

    public Reservation getReservation() {
        return this.reservation;
    }
}
