package auth;

import nextstep.exceptions.exception.RestAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AuthenticationException extends RestAPIException {

    public AuthenticationException() {
        this("인증에 실패하였습니다");
    }

    public AuthenticationException(String responseMessage) {
        super(responseMessage);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
