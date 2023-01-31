package nextstep.exception;

import nextstep.exception.message.ErrorMessage;

public class ThemeException extends BaseException {

    public ThemeException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
