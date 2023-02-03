package auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@Getter
public enum AuthExceptionCode {
    AUTHENTICATION_FAIL(UNAUTHORIZED, "계정을 인증할 수 없습니다."),
    NOT_ADMIN(FORBIDDEN, "관리자만 접근할 수 있습니다.");
    private final HttpStatus httpStatus;
    private final String message;

}
