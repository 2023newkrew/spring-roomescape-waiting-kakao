package nextstep.etc.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"cause", "stackTrace", "localizedMessage", "suppressed"})
public class BaseException extends RuntimeException {

    @Getter
    private final String className;

    @Getter
    private final HttpStatus httpStatus;


    public BaseException(ErrorMessage errorMessage) {
        super(errorMessage.getErrorMessage());

        this.className = getClass().getSimpleName();
        this.httpStatus = errorMessage.getHttpStatus();
    }

    public BaseException(Throwable throwable) {
        super(throwable.getMessage());

        this.className = throwable.getClass().getSimpleName();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BaseException(MethodArgumentNotValidException e) {
        super(getErrorMessage(e));

        this.className = e.getClass().getSimpleName();
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    private static String getErrorMessage(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        BindingResult bindingResult = e.getBindingResult();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            String validationMessage = objectError.getDefaultMessage();
            message.append("[")
                    .append(validationMessage)
                    .append("]");
        }

        return message.toString();
    }
}