package nextstep.exceptions.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CancelReservationStateException extends RestAPIException {

    public CancelReservationStateException() {
        this("예약 취소는 미승인, 승인 상태에서만 진행할 수 있습니다.");
    }

    public CancelReservationStateException(String responseMessage) {
        super(responseMessage);
    }
}
