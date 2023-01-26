package auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends RuntimeException {

    @Getter
    private final HttpStatus httpStatus;

    public AuthenticationException(ErrorMessage errorMessage) {
        super(errorMessage.getErrorMessage());

        this.httpStatus = errorMessage.httpStatus;
    }
}
