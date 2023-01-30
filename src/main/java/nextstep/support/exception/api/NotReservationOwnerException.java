package nextstep.support.exception.api;

import nextstep.support.exception.ErrorCode;

public class NotReservationOwnerException extends ApiException{
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NOT_RESERVATION_OWNER;
    }
}
