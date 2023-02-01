package nextstep.exception;

public class NotCreatorMemberException extends RuntimeException {
    public NotCreatorMemberException() {
        super("예약 대기를 삭제할 수 없습니다.");
    }
}
