package auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "유효하지 않은 토큰입니다.")
public class InvalidTokenException extends RuntimeException {
}

