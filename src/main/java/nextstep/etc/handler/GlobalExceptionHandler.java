package nextstep.etc.handler;

import auth.exception.AuthenticationException;
import nextstep.etc.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> errorHandler(BaseException e) {
        return new ResponseEntity<>(e, e.getHttpStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> errorHandler(AuthenticationException e) {
        BaseException baseException = new BaseException(e);

        return errorHandler(baseException);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> errorHandler(MethodArgumentNotValidException e) {
        BaseException baseException = new BaseException(e);

        return errorHandler(baseException);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> errorHandler(Throwable e) {
        BaseException baseException = new BaseException(e);

        return errorHandler(baseException);
    }
}
