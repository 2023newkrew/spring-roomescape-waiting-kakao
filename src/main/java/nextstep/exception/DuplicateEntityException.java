package nextstep.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEntityException extends BaseWebException {
    public DuplicateEntityException(String actual, String context, Class<?> type) {
        super("중복되지 않아야 합니다.", actual, context, HttpStatus.CONFLICT.value(),type);
    }
}
