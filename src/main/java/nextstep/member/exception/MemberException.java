package nextstep.member.exception;

import nextstep.exception.BaseException;

public class MemberException extends BaseException {

    public MemberException(MemberErrorMessage errorMessage) {
        super(errorMessage);
    }
}
