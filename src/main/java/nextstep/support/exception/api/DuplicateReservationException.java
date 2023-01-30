package nextstep.support.exception.api;

import nextstep.support.exception.ErrorCode;

public class DuplicateReservationException extends ApiException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.DUPLICATE_RESERVATION;
    }
}
