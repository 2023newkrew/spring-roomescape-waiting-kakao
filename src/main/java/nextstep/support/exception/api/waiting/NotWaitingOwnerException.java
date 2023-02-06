package nextstep.support.exception.api.waiting;

import nextstep.support.exception.ErrorCode;
import nextstep.support.exception.api.ApiException;

public class NotWaitingOwnerException extends ApiException {

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NOT_WAITING_OWNER;
    }
}
