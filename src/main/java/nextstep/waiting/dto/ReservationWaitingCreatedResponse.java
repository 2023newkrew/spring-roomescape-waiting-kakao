package nextstep.waiting.dto;

public class ReservationWaitingCreatedResponse {
    private final Long id;
    private final Boolean waiting;

    public ReservationWaitingCreatedResponse(Long id, Boolean waiting) {
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
