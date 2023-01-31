package nextstep.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import nextstep.exception.message.ErrorMessage;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"cause", "stackTrace", "localizedMessage", "suppressed"})
public class BaseException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;

    @Getter
    private final String className;


    public BaseException(String message, HttpStatus httpStatus) {
        super(message);

        this.httpStatus = httpStatus;
        this.className = getClass().getSimpleName();
    }

    public BaseException(String message, HttpStatus httpStatus, String className) {
        super(message);

        this.httpStatus = httpStatus;
        this.className = className;
    }

    public BaseException(ErrorMessage errorMessage) {
        this(errorMessage.getErrorMessage(), errorMessage.getHttpStatus());
    }
}