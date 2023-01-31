package nextstep.exceptions.exception;

import nextstep.exceptions.ErrorCode;

public class AuthorizationException extends CustomException {

    public AuthorizationException() {
        super(ErrorCode.FORBIDDEN_ACCESS);
    }
}
