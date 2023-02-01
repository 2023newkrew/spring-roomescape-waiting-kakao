package nextstep.support.exception.api.reservation;

import nextstep.support.exception.ErrorCode;
import nextstep.support.exception.api.ApiException;

public class IllegalReservationApproveException extends ApiException {

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ILLEGAL_APPROVE_ATTEMPT;
    }
}
