package nextstep.error;

import auth.AuthenticationException;
import auth.AuthorizationException;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import nextstep.error.exception.RoomReservationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Library / Framework

    // 담당 컨트롤러조차 발견하지 못했을 경우 발생
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return handleException(e, ErrorCode.API_NOT_FOUND);
    }

    // 담당 컨트롤러는 존재하나 해당하는 컨트롤러 메소드가 없을 시 발생
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        return handleException(e, ErrorCode.API_NOT_FOUND);
    }

    // url 에 해당하는 컨트롤러 메소드는 존재하나 Http 메소드가 옳바르지 않을 시 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return handleException(e, ErrorCode.API_NOT_FOUND);
    }

    // 요청 시 필요로 하는 body 의 필드의 validation 이 실패했을 시 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return handleException(e, ErrorCode.INVALID_BODY_FIELD);
    }

    // 요청 시 필요로 하는 body 의 필드의 서식(ex: date, time 등)이 잘못되었을 시 발생
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return handleException(e, ErrorCode.INVALID_BODY_FIELD);
    }

    // path variable 및 query parameter 의 validation 이 실패했을 시 발생
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        return handleException(e, ErrorCode.INVALID_PATH_VAR_OR_QUERY_PARAMETER);
    }

    //Auth
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    //Custom

    @ExceptionHandler(RoomReservationException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(RoomReservationException e) {
        return handleException(e, e.getErrorCode());
    }

    // Others

    // 별도 예외 처리 되지 않은 예외들
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception e) {
        return handleException(e, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> handleException(Exception exception, ErrorCode errorCode) {
        log.warn(exception.getMessage(), exception);
        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }
}
