package nextstep.exceptions.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicatedReservationException extends RestAPIException {

    public DuplicatedReservationException() {
        this("중복된 예약이 존재합니다.");
    }

    public DuplicatedReservationException(String responseMessage) {
        super(responseMessage);
    }


}
