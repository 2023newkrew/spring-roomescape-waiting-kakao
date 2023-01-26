package nextstep.error.exception;

import nextstep.error.ErrorCode;

public abstract class CustomException extends RuntimeException {
    protected CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
    }

    public abstract ErrorCode getErrorCode();
}
