package nextstep.error.exception;

import nextstep.error.ErrorCode;

public class RoomReservationException extends RuntimeException {

    private final ErrorCode errorCode;

    public RoomReservationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
