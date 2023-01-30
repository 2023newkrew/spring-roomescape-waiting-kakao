package nextstep.support.exception.api;

import nextstep.support.exception.ErrorCode;

public abstract class ApiException extends RuntimeException{
    public abstract ErrorCode getErrorCode();
}
