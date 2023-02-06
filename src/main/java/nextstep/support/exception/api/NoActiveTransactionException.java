package nextstep.support.exception.api;

import nextstep.support.exception.ErrorCode;

public class NoActiveTransactionException extends ApiException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NO_ACTIVE_TRANSACTION;
    }
}
