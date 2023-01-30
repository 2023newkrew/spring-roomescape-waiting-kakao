package nextstep.support;

import auth.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> onAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(NonExistEntityException.class)
    public ResponseEntity<Void> onNonExistEntityException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Void> onDuplicateEntityException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
