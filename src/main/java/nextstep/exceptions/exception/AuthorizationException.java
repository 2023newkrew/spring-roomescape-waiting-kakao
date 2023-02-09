package nextstep.exceptions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class AuthorizationException extends RestAPIException {

    public AuthorizationException() {
        this("허가되지 않은 사용자 입니다.");
    }

    public AuthorizationException(String responseMessage) {
        super(responseMessage);
    }
}
