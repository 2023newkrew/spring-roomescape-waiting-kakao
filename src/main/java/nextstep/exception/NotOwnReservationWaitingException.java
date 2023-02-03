package nextstep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotOwnReservationWaitingException extends RuntimeException {

    public NotOwnReservationWaitingException() {
    }

    public NotOwnReservationWaitingException(String message) {
        super(message);
    }
}
