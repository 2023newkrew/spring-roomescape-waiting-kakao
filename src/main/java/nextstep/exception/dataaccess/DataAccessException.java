package nextstep.exception.dataaccess;

import org.springframework.http.HttpStatus;

public class DataAccessException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public DataAccessException(DataAccessErrorCode dataAccessErrorCode) {
        super(dataAccessErrorCode.getMessage());
        this.httpStatus = dataAccessErrorCode.getHttpStatus();
        this.message = dataAccessErrorCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
