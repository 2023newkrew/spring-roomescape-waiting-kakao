package nextstep.etc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorMessage {

    RESERVATION_CONFLICT(HttpStatus.CONFLICT, "해당 시각에는 이미 예약이 존재합니다."),
    THEME_CONFLICT(HttpStatus.CONFLICT, "해당 테마가 이미 존재합니다."),
    THEME_NOT_EXISTS(HttpStatus.NOT_FOUND, "존재하지 않는 테마입니다."),
    THEME_RESERVATION_EXISTS(HttpStatus.CONFLICT, "예약이 존재해 삭제할 수 없는 테마입니다.");

    @Getter
    final HttpStatus httpStatus;

    @Getter
    final String errorMessage;

    ErrorMessage(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}