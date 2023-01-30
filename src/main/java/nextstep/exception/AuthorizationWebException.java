package nextstep.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationWebException extends BaseWebException {
    public AuthorizationWebException(String expected, String actual, String context) {
        super(expected, actual, context, HttpStatus.FORBIDDEN.value());
    }
}
