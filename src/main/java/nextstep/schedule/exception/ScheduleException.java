package nextstep.schedule.exception;

import nextstep.exception.BaseException;

public class ScheduleException extends BaseException {

    public ScheduleException(ScheduleErrorMessage errorMessage) {
        super(errorMessage);
    }
}
