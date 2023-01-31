package auth.exception;

import org.springframework.http.HttpStatus;

public enum AuthErrorCode {
    INVALID_USER("인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),
    LOGIN_FAILED_WRONG_USERNAME_PASSWORD("아이디 혹은 비밀번호가 틀렸습니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("해당 URL에 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ;

    private final String message;
    private final HttpStatus httpStatus;

    AuthErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
