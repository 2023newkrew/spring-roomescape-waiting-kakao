package nextstep.reservationwaiting;

public enum ReservationWaitingStatus {

    WAITING("waiting"), RESERVED("reserved"), CANCELED("canceled");

    private String value;

    ReservationWaitingStatus(String value) {
        this.value = value;
    }


}
