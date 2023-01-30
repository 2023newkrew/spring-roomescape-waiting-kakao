package nextstep.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationWebException extends BaseWebException {
    public AuthorizationWebException(String expected, String actual, String context, Class<?> type) {
        super(expected, actual, context, HttpStatus.FORBIDDEN.value(), type);
    }
}
