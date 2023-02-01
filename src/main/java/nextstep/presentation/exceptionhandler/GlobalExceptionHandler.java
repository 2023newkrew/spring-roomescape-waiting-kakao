package nextstep.presentation.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import nextstep.dto.response.ErrorResponse;
import nextstep.error.ApplicationException;
import nextstep.error.ErrorType;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "errorName = {}, errorStatus = {}, errorMessage = {}";

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        ErrorType errorType = e.getErrorType();
        String message = messageSource.getMessage(errorType.name(), null, Locale.KOREA);

        log.error(LOG_FORMAT, errorType.name(), errorType.getHttpStatus(), message);

        return ResponseEntity.status(errorType.getHttpStatus())
                .body(new ErrorResponse(errorType.getHttpStatus(), message));
    }

}
