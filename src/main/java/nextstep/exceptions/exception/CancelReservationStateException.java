package nextstep.exceptions.exception;


public class CancelReservationStateException extends RestAPIException {
    public CancelReservationStateException() {
        super("예약 취소는 미승인, 승인 상태에서만 진행할 수 있습니다.");
    }
}
