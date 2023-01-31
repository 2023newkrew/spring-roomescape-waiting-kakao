package auth.exception;

import org.springframework.http.HttpStatus;

public class AuthAuthorizationException extends AuthWebException {
    public AuthAuthorizationException(String expected, String actual, String context, String type) {
        super(expected, actual, context, type, HttpStatus.FORBIDDEN.value());
    }
}
