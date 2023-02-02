package nextstep.reservation;

public enum ReservationState {
    NOT_APPROVED("NOT_APPROVED"),
    APPROVED("APPROVED"),
    CANCEL_WAITING("CANCEL_WAITING"),
    CANCELED("CANCELED");

    private final String value;

    ReservationState(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}
