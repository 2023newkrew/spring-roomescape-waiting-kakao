package nextstep.support.exception.api;

import nextstep.support.exception.ErrorCode;

public class NoSuchReservationException extends ApiException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NO_SUCH_RESERVATION;
    }
}
