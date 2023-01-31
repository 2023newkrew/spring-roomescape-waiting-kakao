package auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorMessage {
    NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "로그인되지 않은 사용자입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "손상된 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다."),
    NOT_SIGNATURE_TOKEN(HttpStatus.UNAUTHORIZED, "시그니처 검증에 실패한 토큰입니다."),
    INVALID_USERNAME_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "유저이름 또는 비밀번호가 올바르지 않습니다."),

    NOT_ADMIN(HttpStatus.FORBIDDEN, "접근 권한이 없는 사용자입니다.");
    @Getter
    final HttpStatus httpStatus;

    @Getter
    final String errorMessage;

    ErrorMessage(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}