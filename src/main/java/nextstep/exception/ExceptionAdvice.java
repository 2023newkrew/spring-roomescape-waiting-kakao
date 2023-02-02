package nextstep.exception;

import auth.exception.AuthException;
import nextstep.exception.business.BusinessException;
import nextstep.exception.dataaccess.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError()
                .body(ErrorResponse.from("서버에 문제가 생겼습니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(errorMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(e.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ErrorResponse.from(e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ErrorResponse.from(e.getMessage()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ErrorResponse.from(e.getMessage()));
    }
}
