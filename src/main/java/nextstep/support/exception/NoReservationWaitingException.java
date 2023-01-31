package nextstep.support.exception;

public class NoReservationWaitingException extends RuntimeException{
    public NoReservationWaitingException() {
    }

    public NoReservationWaitingException(String message) {
        super(message);
    }
}
