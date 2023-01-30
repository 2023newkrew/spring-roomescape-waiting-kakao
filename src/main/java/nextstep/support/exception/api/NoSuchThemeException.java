package nextstep.support.exception.api;

import nextstep.support.exception.ErrorCode;

public class NoSuchThemeException extends ApiException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NO_SUCH_THEME;
    }
}
