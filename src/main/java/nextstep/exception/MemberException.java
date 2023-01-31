package nextstep.exception;

import nextstep.exception.message.ErrorMessage;

public class MemberException extends BaseException {

    public MemberException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
