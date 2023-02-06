package nextstep.theme.exception;

import nextstep.exception.BaseException;

public class ThemeException extends BaseException {

    public ThemeException(ThemeErrorMessage errorMessage) {
        super(errorMessage);
    }
}
