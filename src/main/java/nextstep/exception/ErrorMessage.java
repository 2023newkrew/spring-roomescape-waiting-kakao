package nextstep.exception;

import org.springframework.http.HttpStatus;

public interface ErrorMessage {

    HttpStatus getHttpStatus();

    String getErrorMessage();
}
