package nextstep.exception.handler;

import nextstep.exception.AuthorizationWebException;
import nextstep.exception.BaseWebException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BaseExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> onException(Exception e) {
        e.printStackTrace();
        logger.error(e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(BaseWebException.class)
    public ResponseEntity<String> onBaseException(BaseWebException e) {
        logger.error(e.getFullMessage());
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(AuthorizationWebException.class)
    public ResponseEntity<String> onUnAuthorizationException(AuthorizationWebException e) {
        logger.error(e.getFullMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
