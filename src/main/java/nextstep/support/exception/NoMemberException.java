package nextstep.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoMemberException extends RuntimeException {
    public NoMemberException() {
    }

    public NoMemberException(String message) {
        super(message);
    }
}
