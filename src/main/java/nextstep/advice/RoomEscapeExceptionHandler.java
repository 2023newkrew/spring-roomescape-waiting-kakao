package nextstep.advice;

import nextstep.error.ErrorCode;
import nextstep.error.ErrorResponse;
import nextstep.exception.RoomEscapeException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomEscapeExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(RoomEscapeException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        String errorMessage = generateErrorMessage(errorCode, exception);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getCode(), errorMessage));
    }

    private String generateErrorMessage(ErrorCode errorCode, RoomEscapeException exception) {
        StringBuilder stringBuilder = new StringBuilder(errorCode.getDescription());
        if (StringUtils.hasText(exception.getMessage())) {
            stringBuilder.append(" - ").append(exception.getMessage());
        }
        return stringBuilder.toString();
    }
}
