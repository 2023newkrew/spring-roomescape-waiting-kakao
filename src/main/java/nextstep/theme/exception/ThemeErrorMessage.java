package nextstep.theme.exception;


import lombok.Getter;
import nextstep.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public enum ThemeErrorMessage implements ErrorMessage {

    CONFLICT(HttpStatus.CONFLICT, "해당 테마가 이미 존재합니다."),
    NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 테마입니다."),
    RESERVATION_EXISTS(HttpStatus.CONFLICT, "예약이 존재해 삭제할 수 없는 테마입니다.");

    @Getter
    final HttpStatus httpStatus;

    @Getter
    final String errorMessage;

    ThemeErrorMessage(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
