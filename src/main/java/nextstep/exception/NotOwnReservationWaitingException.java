package nextstep.exception;

public class NotOwnReservationWaitingException extends RuntimeException{
    public NotOwnReservationWaitingException() {
    }

    public NotOwnReservationWaitingException(String message) {
        super(message);
    }
}
