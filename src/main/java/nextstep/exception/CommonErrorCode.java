package nextstep.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {

    DUPLICATE_ENTITY(HttpStatus.CONFLICT, "중복된 항목이 있습니다."),

    NOT_EXIST_ENTITY(HttpStatus.NOT_FOUND, "찾을 수 없는 항목입니다."),

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    BAD_PARAMETER_REQUEST(HttpStatus.BAD_REQUEST, "처리할 수 없는 요청입니다.");

    private final HttpStatus httpStatus;

    private final String message;

    CommonErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
