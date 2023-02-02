package nextstep.exception;

import nextstep.error.ErrorCode;

public class UnauthenticatedException extends RoomEscapeException {
    private ErrorCode errorCode;

    public UnauthenticatedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthenticatedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UnauthenticatedException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public UnauthenticatedException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
