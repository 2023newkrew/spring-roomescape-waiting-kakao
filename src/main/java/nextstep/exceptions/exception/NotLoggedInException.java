package nextstep.exceptions.exception;

import nextstep.exceptions.ErrorCode;

public class NotLoggedInException extends CustomException {

    public NotLoggedInException() {
        super(ErrorCode.NOT_LOGGED_IN);
    }
}
