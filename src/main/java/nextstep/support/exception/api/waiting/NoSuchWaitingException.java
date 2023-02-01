package nextstep.support.exception.api.waiting;

import nextstep.support.exception.ErrorCode;
import nextstep.support.exception.api.ApiException;

public class NoSuchWaitingException extends ApiException {

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NO_SUCH_WAITING;
    }
}
