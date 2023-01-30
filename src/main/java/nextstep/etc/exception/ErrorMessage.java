package nextstep.etc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorMessage {
    MEMBER_CONFLICT(HttpStatus.CONFLICT, "유저이름이 이미 존재합니다."),
    THEME_CONFLICT(HttpStatus.CONFLICT, "해당 테마가 이미 존재합니다."),
    THEME_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 테마입니다."),
    THEME_RESERVATION_EXISTS(HttpStatus.CONFLICT, "예약이 존재해 삭제할 수 없는 테마입니다."),
    SCHEDULE_CONFLICT(HttpStatus.CONFLICT, "동일한 일정이 존재합니다."),
    SCHEDULE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "일정이 존재하지 않습니다."),
    RESERVATION_CONFLICT(HttpStatus.CONFLICT, "이미 예약된 일정입니다."),
    WAITING_CONFLICT(HttpStatus.CONFLICT, "이미 예약 대기된 일정입니다."),
    RESERVATION_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 예약입니다."),
    WAITING_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 예약 대기입니다."),
    NOT_RESERVATION_OWNER(HttpStatus.FORBIDDEN, "예약한 사용자가 아닙니다."),
    NOT_WAITING_OWNER(HttpStatus.FORBIDDEN, "예약 대기한 사용자가 아닙니다.");

    @Getter
    final HttpStatus httpStatus;

    @Getter
    final String errorMessage;

    ErrorMessage(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}