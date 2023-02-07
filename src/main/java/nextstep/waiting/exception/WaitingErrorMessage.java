package nextstep.waiting.exception;


import lombok.Getter;
import nextstep.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public enum WaitingErrorMessage implements ErrorMessage {

    ALREADY_RESERVED(HttpStatus.CONFLICT, "이미 예약중인 스케줄입니다."),
    CONFLICT(HttpStatus.CONFLICT, "이미 예약 대기중인 스케줄입니다."),
    NOT_EXISTS(HttpStatus.BAD_REQUEST, "예약 대기가 존재하지 않습니다."),
    NOT_OWNER(HttpStatus.FORBIDDEN, "자신이 생성한 예약 대기가 아닙니다.");

    @Getter
    final HttpStatus httpStatus;

    @Getter
    final String errorMessage;

    WaitingErrorMessage(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
