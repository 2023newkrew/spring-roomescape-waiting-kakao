package auth.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public AuthException(AuthErrorCode authErrorCode) {
        super(authErrorCode.getMessage());
        this.httpStatus = authErrorCode.getHttpStatus();
        this.message = authErrorCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
