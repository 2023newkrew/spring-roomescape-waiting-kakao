package nextstep.support.exception.api.reservation;

import nextstep.support.exception.ErrorCode;
import nextstep.support.exception.api.ApiException;

public class DuplicateReservationException extends ApiException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.DUPLICATE_RESERVATION;
    }
}
