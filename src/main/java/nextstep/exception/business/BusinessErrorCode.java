package nextstep.exception.business;

import org.springframework.http.HttpStatus;

public enum BusinessErrorCode {
    DELETE_FAILED_WHEN_NOT_MY_RESERVATION("본인의 예약만 삭제할 수 있습니다.", HttpStatus.UNAUTHORIZED),
    RESERVATION_ALREADY_EXIST_AT_THAT_TIME("해당 시간에 이미 예약이 존재합니다.", HttpStatus.CONFLICT),
    MEMBER_ALREADY_EXIST_BY_USERNAME("해당 username을 가진 유저가 이미 존재합니다..", HttpStatus.CONFLICT),
    ;

    private final String message;
    private final HttpStatus httpStatus;

    BusinessErrorCode(String message, HttpStatus httpStatus) {
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
