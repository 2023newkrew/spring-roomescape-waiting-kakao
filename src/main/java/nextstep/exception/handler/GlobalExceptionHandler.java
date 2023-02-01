package nextstep.exception.handler;

import auth.exception.AuthWebException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(AuthWebException.class)
    public ResponseEntity<String> onAuthWebException(AuthWebException e) {
        logger.error(e.getFullMessage());
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

}
