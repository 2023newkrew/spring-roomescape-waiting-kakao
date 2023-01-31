package nextstep.exception;

import nextstep.exception.message.ErrorMessage;

public class ScheduleException extends BaseException {

    public ScheduleException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
