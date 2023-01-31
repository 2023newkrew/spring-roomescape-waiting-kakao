package nextstep.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoReservationException extends RuntimeException {
    public NoReservationException() {
    }

    public NoReservationException(String message) {
        super(message);
    }
}
