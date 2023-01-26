package nextstep.exceptions.exception;

import org.springframework.http.HttpStatus;

public abstract class RestAPIException extends RuntimeException {

    public abstract HttpStatus getHttpStatus();

    public RestAPIException(String responseMessage) {
        super(responseMessage);
    }
}
