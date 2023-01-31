package nextstep.exceptions.exception;

import nextstep.exceptions.ErrorCode;

public class DuplicatedEntityException extends CustomException {

    public DuplicatedEntityException() {
        super(ErrorCode.DUPLICATED_ENTITY);
    }
}
