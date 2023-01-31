package nextstep.reservation.exception;


import lombok.Getter;
import nextstep.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public enum ReservationErrorMessage implements ErrorMessage {

    CONFLICT(HttpStatus.CONFLICT, "이미 예약된 일정입니다."),
    NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 예약입니다."),
    NOT_OWNER(HttpStatus.FORBIDDEN, "예약한 사용자가 아닙니다.");

    @Getter
    final HttpStatus httpStatus;

    @Getter
    final String errorMessage;

    ReservationErrorMessage(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
