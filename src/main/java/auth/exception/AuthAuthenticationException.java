package auth.exception;

import org.springframework.http.HttpStatus;

public class AuthAuthenticationException extends AuthWebException {

    public AuthAuthenticationException(String expected, String actual, String context, String type) {
        super(expected,actual,context,type,HttpStatus.UNAUTHORIZED.value());
    }

}
