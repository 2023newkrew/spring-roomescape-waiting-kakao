package nextstep.exceptions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundObjectException extends RestAPIException {

    public NotFoundObjectException() {
        this("객체를 찾을 수 없습니다.");
    }

    public NotFoundObjectException(String responseMessage) {
        super(responseMessage);
    }

}
