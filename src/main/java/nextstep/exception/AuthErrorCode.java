package nextstep.exception;

import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ErrorCode {

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "아이디 혹은 비밀번호가 잘못됐습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "수행 권한이 없습니다."),

    TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "토큰이 필요합니다."),

    UNAUTHORIZED_DELETE(HttpStatus.FORBIDDEN, "본인만 예약을 삭제할 수 있습니다.");

    private final HttpStatus httpStatus;

    private final String message;

    AuthErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
