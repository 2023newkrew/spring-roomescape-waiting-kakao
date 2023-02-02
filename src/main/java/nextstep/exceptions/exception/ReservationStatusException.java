package nextstep.exceptions.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ReservationStatusException extends RestAPIException {

    public ReservationStatusException(String responseMessage) {
        super(responseMessage);
    }
}
