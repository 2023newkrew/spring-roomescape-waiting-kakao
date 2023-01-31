package nextstep.exceptions.exception;

import nextstep.exceptions.ErrorCode;

public class LoginFailException extends CustomException {

    public LoginFailException() {
        super(ErrorCode.LOGIN_FAIL);
    }
}
