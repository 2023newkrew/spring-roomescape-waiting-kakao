package nextstep.support.exception.api;

import nextstep.support.exception.ErrorCode;

public class IllegalApproveException extends ApiException{

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ILLEGAL_APPROVE_ATTEMPT;
    }
}
