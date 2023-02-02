package nextstep.exception;

import nextstep.error.ErrorCode;

public class RoomEscapeException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public RoomEscapeException() {
        super();
    }

    public RoomEscapeException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public RoomEscapeException(ErrorCode errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.message = message;
    }

    public RoomEscapeException(ErrorCode errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public RoomEscapeException(ErrorCode errorCode, Throwable cause) {
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

