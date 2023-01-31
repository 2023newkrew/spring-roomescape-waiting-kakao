package nextstep.exceptions.exception;

import org.springframework.http.HttpStatus;

public class NotFoundObjectException extends RestAPIException {

    public NotFoundObjectException() {
        this("객체를 찾을 수 없습니다.");
    }

    public NotFoundObjectException(String responseMessage) {
        super(responseMessage);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }
}
