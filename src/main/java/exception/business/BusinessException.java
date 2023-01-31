package exception.business;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

    public BusinessException(BusinessErrorCode businessErrorCode) {
        super(businessErrorCode.getMessage());
        this.httpStatus = businessErrorCode.getHttpStatus();
        this.message = businessErrorCode.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
