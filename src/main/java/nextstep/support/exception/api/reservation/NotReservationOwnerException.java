package nextstep.support.exception.api.reservation;

import nextstep.support.exception.ErrorCode;
import nextstep.support.exception.api.ApiException;

public class NotReservationOwnerException extends ApiException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NOT_RESERVATION_OWNER;
    }
}
