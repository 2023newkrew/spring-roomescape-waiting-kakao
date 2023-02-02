package nextstep.support;

public class UnsupportedReservationStatusException extends IllegalArgumentException {

    public UnsupportedReservationStatusException() {
    }

    public UnsupportedReservationStatusException(String s) {
        super(s);
    }

}
