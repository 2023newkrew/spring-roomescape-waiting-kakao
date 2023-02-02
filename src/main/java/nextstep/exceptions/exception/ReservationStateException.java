package nextstep.exceptions.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ReservationStateException extends RestAPIException {

    public ReservationStateException(String responseMessage) {
        super(responseMessage);
    }
}
