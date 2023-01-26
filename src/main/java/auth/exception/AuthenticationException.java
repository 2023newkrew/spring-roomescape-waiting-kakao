package auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public AuthenticationException(ErrorMessage errorMessage) {
        super(errorMessage.getErrorMessage());

        this.status = errorMessage.httpStatus;
    }
}
