package nextstep;

import auth.exception.AuthenticationException;
import nextstep.exception.RoomEscapeException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(RoomEscapeException.class)
    public ResponseEntity<String> onDuplicateEntityException(RoomEscapeException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
