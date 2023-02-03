package nextstep.exception;

import auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({
            NotCreatorMemberException.class, DuplicateEntityException.class, NotFoundEntityException.class
    })
    public ResponseEntity<ErrorResponse> onBadRequest(Exception e) {
        LOGGER.debug(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> onAuthenticationException(Exception e) {
        LOGGER.debug(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> onInternalServerError(Exception e) {
        LOGGER.error(e.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponse("요청을 처리할 수 없습니다."));
    }
}
