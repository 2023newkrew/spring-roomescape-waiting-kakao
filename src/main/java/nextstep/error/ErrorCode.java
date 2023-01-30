package nextstep.error;

public enum ErrorCode {
    RESERVATION_NOT_FOUND(400, "해당 예약을 찾을 수 없습니다"),
    DUPLICATE_RESERVATION(400, "같은 시간에 예약이 존재합니다"),
    NO_RESERVATION(400, "해당 시간에 예약이 존재하지 않아 대기를 생성할 수 없습니다"),
    WAITING_NOT_FOUND(400, "해당 예약 대기를 찾을 수 없습니다"),
    THEME_NOT_FOUND(400, "해당 테마를 찾을 수 없습니다"),
    SCHEDULE_NOT_FOUND(400, "해당 스케줄을 찾을 수 없습니다"),
    AUTHENTICATION(401, "로그인 실패"),
    AUTHORIZATION(403, "권한 없음");

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