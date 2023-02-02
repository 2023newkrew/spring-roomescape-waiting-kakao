package nextstep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RoomEscapeException.class)
    public ResponseEntity<ErrorResponse> roomEscapeExceptionHandler(RoomEscapeException e) {
        e.printStackTrace();
        String message = e.getMessage();
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus statusCode = errorCode.getStatusCode();
        ErrorResponse res = new ErrorResponse(errorCode.toString(), message);
        return ResponseEntity.status(statusCode).body(res);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> ExceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
