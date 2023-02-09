package nextstep.exceptions.exception;

public class InvalidReservationStatusException extends RestAPIException{

    public InvalidReservationStatusException() {
        super("존재하지 않는 예약 상태입니다.");
    }

}
