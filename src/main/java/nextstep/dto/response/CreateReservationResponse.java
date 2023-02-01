package nextstep.dto.response;

public class CreateReservationResponse {

    private Long id;
    private boolean isCreated;

    public CreateReservationResponse(Long id, boolean isCreated) {
        this.id = id;
        this.isCreated = isCreated;
    }

    public Long getId() {
        return id;
    }

    public boolean isReservationCreated() {
        return isCreated;
    }
}
