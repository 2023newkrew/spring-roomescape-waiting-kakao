package nextstep.error.exception;

import nextstep.error.ErrorCode;

public class EntityNotFoundException extends CustomException {
    private final ErrorCode errorCode;

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode, null);
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
