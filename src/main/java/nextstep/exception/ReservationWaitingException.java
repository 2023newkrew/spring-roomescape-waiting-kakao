package nextstep.exception;

public class ReservationWaitingException extends RoomEscapeException {
    public ReservationWaitingException(RoomEscapeExceptionCode roomEscapeExceptionCode) {
        super(roomEscapeExceptionCode);
    }
}
