package nextstep.error;

import org.springframework.http.HttpStatus;

public enum ErrorType {

    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST.value()),
    THEME_NOT_FOUND(HttpStatus.BAD_REQUEST.value()),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST.value()),
    RESERVATION_DEPOSIT_NOT_ENOUGH(HttpStatus.BAD_REQUEST.value()),
    RESERVATION_WAITING_NOT_FOUND(HttpStatus.BAD_REQUEST.value()),
    SCHEDULE_NOT_FOUND(HttpStatus.BAD_REQUEST.value()),
    DUPLICATE_THEME(HttpStatus.BAD_REQUEST.value()),
    DUPLICATE_RESERVATION(HttpStatus.BAD_REQUEST.value()),
    INVALID_PASSWORD_ERROR(HttpStatus.BAD_REQUEST.value()),
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED.value()),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value()),
    JSON_CONVERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value()),
    ;

    private int httpStatus;

    ErrorType(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
