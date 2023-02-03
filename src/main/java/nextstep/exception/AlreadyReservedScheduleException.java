package nextstep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyReservedScheduleException extends RuntimeException {

    public AlreadyReservedScheduleException() {
    }

    public AlreadyReservedScheduleException(String message) {
        super(message);
    }
}
