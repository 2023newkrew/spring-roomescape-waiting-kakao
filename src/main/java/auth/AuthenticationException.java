package auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        this("인증에 실패하였습니다");
    }

    public AuthenticationException(String responseMessage) {
        super(responseMessage);
    }

}
