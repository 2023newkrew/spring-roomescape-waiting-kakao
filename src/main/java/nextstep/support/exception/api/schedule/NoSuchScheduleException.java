package nextstep.support.exception.api.schedule;

import nextstep.support.exception.ErrorCode;
import nextstep.support.exception.api.ApiException;

public class NoSuchScheduleException extends ApiException {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NO_SUCH_SCHEDULE;
    }
}
