package nextstep.etc.exception;

public class AuthenticationException extends BaseException {

    public AuthenticationException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
