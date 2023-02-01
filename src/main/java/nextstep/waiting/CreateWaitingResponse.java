package nextstep.waiting;

import nextstep.reservation.Reservation;

public class CreateWaitingResponse {
    private final boolean isReservedDirectly;
    private final Long id;

    private CreateWaitingResponse(boolean isReservedDirectly, Long id) {
        this.isReservedDirectly = isReservedDirectly;
        this.id = id;
    }

    public static CreateWaitingResponse fromReservation(Reservation reservation) {
        return new CreateWaitingResponse(true, reservation.getId());
    }

    public static CreateWaitingResponse fromWaiting(Waiting waiting) {
        return new CreateWaitingResponse(false, waiting.getId());
    }

    public boolean isReservedDirectly() {
        return isReservedDirectly;
    }

    public Long getId() {
        return id;
    }
}
