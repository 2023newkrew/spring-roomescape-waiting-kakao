package roomwaiting.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.NotAcceptableStatusException;


@RestControllerAdvice
public class ExceptionHandlers {
    private static final Logger logger =
            LoggerFactory.getLogger(ExceptionHandlers.class);

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<Object> authenticationException(final AuthenticationServiceException ex) {
        logger.warn(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NullPointerException.class )
    public ResponseEntity<Object> badRequestException(final NullPointerException ex) {
        logger.warn(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationServiceException.class )
    public ResponseEntity<Object> unAuthorizedException(final AuthorizationServiceException ex) {
        logger.warn(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> duplicateException(final DuplicateKeyException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(NotAcceptableStatusException.class)
    public ResponseEntity<Object> notAcceptableStatusException(final NotAcceptableStatusException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }
}