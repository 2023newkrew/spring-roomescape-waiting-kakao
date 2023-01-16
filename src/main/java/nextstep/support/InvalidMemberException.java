package nextstep.support;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "아이디 혹은 패스워드가 잘못되었습니다.")
public class InvalidMemberException extends RuntimeException{
}
