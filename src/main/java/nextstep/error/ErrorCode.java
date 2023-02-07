package nextstep.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 공통 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러입니다. 관리자에게 문의해 주세요."),
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "잘못된 API 요청입니다."),
    INVALID_BODY_FIELD(HttpStatus.BAD_REQUEST, "바디의 필드가 잘못된 형식이거나 누락되었습니다."),
    INVALID_PATH_VAR_OR_QUERY_PARAMETER(HttpStatus.BAD_REQUEST, "경로 변수나 쿼리 파라미터가 잘못된 형식이거나 누락되었습니다."),
    INVALID_UNLOCK(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 동기화 처리입니다. 관리자에게 문의해주세요."),

    // 인증 에러
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "유저네임 또는 패스워드가 틀렸습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    ADMIN_AUTHORITY_REQUIRED(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),

    // 회원 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 ID의 회원이 존재하지 않습니다."),
    MEMBER_CANT_BE_DELETED(HttpStatus.BAD_REQUEST, "회원 삭제 전 예약이 먼저 삭제되어야 합니다."),

    // 예약 에러
    DUPLICATE_RESERVATION(HttpStatus.BAD_REQUEST, "해당 시간에 예약이 존재합니다."),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 ID의 예약이 존재하지 않습니다."),

    // 예약 대기 에러
    DUPLICATE_RESERVATION_WAITING(HttpStatus.BAD_REQUEST, "해당 시간에 대한 예약 대기 요청이 존재합니다."),
    RESERVATION_WAITING_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 ID의 예약 대기가 존재하지 않습니다."),

    // 시간 에러
    SCHEDULE_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 스케줄은 존재하지 않습니다."),
    SCHEDULE_CANT_BE_DELETED(HttpStatus.BAD_REQUEST, "스케줄 작제 전 예약이 먼저 삭제되어야 합니다."),

    // 테마 에러
    THEME_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 ID의 테마가 존재하지 않습니다."),
    THEME_CANT_BE_DELETED(HttpStatus.BAD_REQUEST, "테마 삭제 전 스케줄이 먼저 삭제되어야 합니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
