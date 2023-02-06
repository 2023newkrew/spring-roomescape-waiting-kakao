package nextstep.support.exception.api.reservation;

import nextstep.support.exception.ErrorCode;
import nextstep.support.exception.api.ApiException;

public class IllegalReservationCancelException extends ApiException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ILLEGAL_CANCEL_ATTEMPT;
    }
}
