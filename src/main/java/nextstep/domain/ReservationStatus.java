package nextstep.domain;

public enum ReservationStatus {
    CREATED("CREATED"),
    APPROVED("APPROVED"),
    CANCELLED("CANCELLED"),
    REQUESTED_CANCEL("REQUESTED_CANCEL"),
    REJECTED("REJECTED");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
