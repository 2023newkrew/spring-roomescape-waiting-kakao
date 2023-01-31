package nextstep.exception.handler;

import auth.exception.ForbiddenException;
import auth.exception.UnauthorizedException;
import nextstep.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseException> errorHandler(BaseException e) {
        return new ResponseEntity<>(e, e.getHttpStatus());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<BaseException> errorHandler(UnauthorizedException e) {
        return errorHandler(convertFrom(e, HttpStatus.UNAUTHORIZED));
    }

    private BaseException convertFrom(Throwable e, HttpStatus httpStatus) {
        String message = e.getMessage();
        String className = e.getClass().getSimpleName();

        return new BaseException(message, httpStatus, className);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<BaseException> errorHandler(ForbiddenException e) {
        return errorHandler(convertFrom(e, HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseException> errorHandler(MethodArgumentNotValidException e) {
        return errorHandler(convertFromValidException(e));
    }

    private BaseException convertFromValidException(MethodArgumentNotValidException e) {
        String message = getErrorMessage(e);
        String className = e.getClass().getSimpleName();

        return new BaseException(message, HttpStatus.BAD_REQUEST, className);
    }

    private String getErrorMessage(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        BindingResult bindingResult = e.getBindingResult();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            String validationMessage = "[" + objectError.getDefaultMessage() + "]";
            message.append(validationMessage);
        }

        return message.toString();
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<BaseException> errorHandler(Throwable e) {
        return errorHandler(convertFrom(e, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
