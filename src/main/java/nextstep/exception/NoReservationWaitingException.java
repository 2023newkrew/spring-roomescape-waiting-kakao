package nextstep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoReservationWaitingException extends RuntimeException {

    public NoReservationWaitingException() {
    }

    public NoReservationWaitingException(String message) {
        super(message);
    }
}
