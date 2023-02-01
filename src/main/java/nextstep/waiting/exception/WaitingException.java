package nextstep.waiting.exception;

import nextstep.exception.BaseException;

public class WaitingException extends BaseException {

    public WaitingException(WaitingErrorMessage errorMessage) {
        super(errorMessage);
    }
}
