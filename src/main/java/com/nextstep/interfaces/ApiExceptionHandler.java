package com.nextstep.interfaces;

import com.authorizationserver.domains.authorization.exceptions.AuthenticationException;
import com.nextstep.domains.exceptions.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ApiExceptionHandler {

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
