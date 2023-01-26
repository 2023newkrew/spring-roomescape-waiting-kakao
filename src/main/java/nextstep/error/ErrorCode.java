package nextstep.error;

public enum ErrorCode {
    RESERVATION_NOT_FOUND(400, "해당 예약을 찾을 수 없습니다"),
    DUPLICATE_RESERVATION(400, "같은 시간에 예약이 존재합니다");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}