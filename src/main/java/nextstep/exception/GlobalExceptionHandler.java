package nextstep.exception;

import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.exception.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({NotExistEntityException.class, DuplicateEntityException.class})
    public ResponseEntity<Void> onDuplicatedAndNotExistEntityException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Void> onAuthenticationException(AuthenticationException e) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AuthorizationException.class})
    public ResponseEntity<Void> onAuthorizationException(AuthorizationException e) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Void> onRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().build();
    }
}
