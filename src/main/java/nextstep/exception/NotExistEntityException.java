package nextstep.exception;

import nextstep.error.ErrorCode;

public class NotExistEntityException extends RoomEscapeException {

    private ErrorCode errorCode;

    public NotExistEntityException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotExistEntityException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public NotExistEntityException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public NotExistEntityException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
