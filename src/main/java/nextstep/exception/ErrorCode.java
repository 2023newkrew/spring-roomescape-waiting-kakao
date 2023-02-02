package nextstep.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ENTITY_NOT_EXISTS(HttpStatus.NOT_FOUND, "해당 항목이 존재하지 않습니다."),
    FAILED_TO_LOGIN(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    SCHEDULE_ALREADY_EXISTS(HttpStatus.CONFLICT, "해당 스케줄이 이미 존재합니다.");

    private final HttpStatus statusCode;
    private final String message;

    ErrorCode(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return this.message;
    }
}
