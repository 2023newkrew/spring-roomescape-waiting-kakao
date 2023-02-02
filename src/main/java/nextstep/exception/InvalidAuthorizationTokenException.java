package nextstep.exception;

import nextstep.error.ErrorCode;

public class InvalidAuthorizationTokenException extends RoomEscapeException {

    private ErrorCode errorCode;

    public InvalidAuthorizationTokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidAuthorizationTokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public InvalidAuthorizationTokenException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public InvalidAuthorizationTokenException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
