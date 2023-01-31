package nextstep.exceptions;

import nextstep.exceptions.exception.CustomException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<Object> handleException(CustomException e) {
        return ResponseEntity
                .status(HttpStatus.valueOf(e.getErrorCode().getStatus()))
                .body(new ErrorResponse(e.getErrorCode()));
    }

    @ExceptionHandler(value = DataAccessException.class)
    protected ResponseEntity<Object> handleDataAccessException() {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.DATA_ACCESS_ERROR);
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalStateException(IllegalStateException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.WAITING_UNAVAILABLE);
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = ResponseStatusException.class)
    protected ResponseEntity<Object> handleException(ResponseStatusException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getRawStatusCode(), e.getStatus().name(), e.getReason()));
    }
}
