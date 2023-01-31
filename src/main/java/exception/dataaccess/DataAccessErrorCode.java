package exception.dataaccess;

import org.springframework.http.HttpStatus;

public enum DataAccessErrorCode {
    MEMBER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    RESERVATION_NOT_FOUND("예약을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SCHEDULE_NOT_FOUND("스케줄을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    THEME_NOT_FOUND("테마를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ;

    private final String message;
    private final HttpStatus httpStatus;

    DataAccessErrorCode(String message, HttpStatus httpStatus) {
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
