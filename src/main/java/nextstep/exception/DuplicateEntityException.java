package nextstep.exception;

import nextstep.error.ErrorCode;

public class DuplicateEntityException extends RoomEscapeException {
    private ErrorCode errorCode;

    public DuplicateEntityException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DuplicateEntityException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public DuplicateEntityException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public DuplicateEntityException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
