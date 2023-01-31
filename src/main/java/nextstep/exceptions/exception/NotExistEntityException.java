package nextstep.exceptions.exception;

import nextstep.exceptions.ErrorCode;

public class NotExistEntityException extends CustomException {

    public NotExistEntityException() {
        super(ErrorCode.NOT_EXIST_ENTITY);
    }
}

