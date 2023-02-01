package nextstep.exceptions.exception;

public class InvalidReservationStateException extends RestAPIException{

    public InvalidReservationStateException() {
        super("존재하지 않는 예약 상태입니다.");
    }

}
