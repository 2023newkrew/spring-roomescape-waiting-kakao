package auth.exception;

import nextstep.error.ErrorCode;

public class AuthException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public AuthException() {
        super();
    }

    public AuthException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public AuthException(ErrorCode errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    public AuthException(ErrorCode errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public AuthException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

