package nextstep.support.exception.api;

import nextstep.support.exception.ErrorCode;

public class IllegalCancelException extends ApiException{
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ILLEGAL_CANCEL_ATTEMPT;
    }
}
