package nextstep.schedule.exception;


import lombok.Getter;
import nextstep.exception.ErrorMessage;
import org.springframework.http.HttpStatus;

public enum ScheduleErrorMessage implements ErrorMessage {

    CONFLICT(HttpStatus.CONFLICT, "동일한 일정이 존재합니다."),
    NOT_EXISTS(HttpStatus.BAD_REQUEST, "일정이 존재하지 않습니다.");
    @Getter
    final HttpStatus httpStatus;

    @Getter
    final String errorMessage;

    ScheduleErrorMessage(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
