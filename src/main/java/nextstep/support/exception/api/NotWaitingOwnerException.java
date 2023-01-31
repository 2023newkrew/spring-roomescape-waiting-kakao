package nextstep.support.exception.api;

import nextstep.support.exception.ErrorCode;

public class NotWaitingOwnerException extends ApiException {

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NOT_WAITING_OWNER;
    }
}
