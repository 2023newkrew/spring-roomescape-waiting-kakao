package nextstep.exception;

import nextstep.error.ErrorCode;

public class UnauthorizedException extends RoomEscapeException {

    private ErrorCode errorCode;

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UnauthorizedException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public UnauthorizedException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
