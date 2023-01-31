package nextstep.exception;

public class MemberException extends RoomEscapeException {
    public MemberException(RoomEscapeExceptionCode roomEscapeExceptionCode) {
        super(roomEscapeExceptionCode);
    }
}
