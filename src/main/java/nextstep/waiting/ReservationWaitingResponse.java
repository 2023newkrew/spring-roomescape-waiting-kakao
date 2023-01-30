package nextstep.waiting;

public class ReservationWaitingResponse {
    private final Long id;
    private final Boolean waiting;

    public ReservationWaitingResponse(Long id, Boolean waiting) {
        this.id = id;
        this.waiting = waiting;
    }

    public Long getId() {
        return id;
    }

    public Boolean getWaiting() {
        return waiting;
    }
}
