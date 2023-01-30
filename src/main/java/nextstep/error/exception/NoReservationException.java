package nextstep.error.exception;


import nextstep.error.ErrorCode;

public class NoReservationException extends CustomException {
    private final ErrorCode errorCode = ErrorCode.NO_RESERVATION;

    public NoReservationException() {
        super(ErrorCode.NO_RESERVATION, null);
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
