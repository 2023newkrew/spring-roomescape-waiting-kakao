package nextstep.support.exception.api.reservation;

import nextstep.support.exception.ErrorCode;
import nextstep.support.exception.api.ApiException;

public class NoSuchReservationException extends ApiException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NO_SUCH_RESERVATION;
    }
}
