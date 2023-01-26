package nextstep.error.exception;

import nextstep.error.ErrorCode;

public class DuplicateEntityException extends CustomException {
    private final ErrorCode errorCode;

    public DuplicateEntityException(ErrorCode errorCode) {
        super(errorCode, null);
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
