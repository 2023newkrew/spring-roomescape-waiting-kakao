package auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthenticationException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public AuthenticationException(AuthExceptionCode authExceptionCode) {
        super(authExceptionCode.getMessage());
        this.httpStatus = authExceptionCode.getHttpStatus();
        this.message = authExceptionCode.getMessage();
    }
}
