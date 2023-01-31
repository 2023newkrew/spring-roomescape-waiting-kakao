package nextstep.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseWebException {

    public NotFoundException(String expected, String actual, String context,String  type) {
        super(expected, actual, context, HttpStatus.NOT_FOUND.value(), type);
    }
}
