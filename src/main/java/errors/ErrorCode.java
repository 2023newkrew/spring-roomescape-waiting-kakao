package errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 예약 관련 에러
    ALREADY_EXIST_RESERVATION_AT_TIME(HttpStatus.CONFLICT, "R-000", "이미 해당 시간대에 예약이 존재합니다."),
    UNKNOWN_RESERVATION_ID(HttpStatus.NOT_FOUND, "R-001", "해당 ID는 유효하지 않습니다."),
    IMPOSSIBLE_APPROVE(HttpStatus.CONFLICT, "R-002", "해당 예약은 승인할 수 없는 상태입니다."),
    IMPOSSIBLE_CANCEL(HttpStatus.CONFLICT, "R-003", "해당 예약은 취소할 수 없는 상태입니다."),
    IMPOSSIBLE_DISAPPROVE(HttpStatus.CONFLICT, "R-004", "해당 예약은 거절할 수 없는 상태입니다."),
    IMPOSSIBLE_CANCEL_ACCEPT(HttpStatus.CONFLICT, "R-005", "해당 예약은 취소허가 할 수 없는 상태입니다."),

    // 대기 관련 에러
    UNKNOWN_WAITING_ID(HttpStatus.NOT_FOUND, "W-001", "해당 ID는 유효하지 않습니다."),
    // 테마 관련 에러
    UNKNOWN_THEME_ID(HttpStatus.NOT_FOUND, "T-000", "알 수 없는 테마 ID입니다."),
    USING_THEME(HttpStatus.CONFLICT, "T-001", "사용중인 테마입니다. 사용중 테마는 삭제 불가능합니다."),
    // 인증, 권한 관련 에러
    REQUIRED_ADMIN(HttpStatus.UNAUTHORIZED, "A-000", "어드민만 해당 요청을 수행 가능합니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A-001", "만료된 토큰입니다."),
    INVALID_LOGIN_INFORMATION(HttpStatus.UNAUTHORIZED, "A-002", "올바르지 않은 아이디, 혹은 비밀번호입니다."),
    UNAUTHORIZED_RESERVATION(HttpStatus.FORBIDDEN, "A-003", "해당 예약에 대해 권한이 없습니다."),
    UNAUTHORIZED_WAITING(HttpStatus.FORBIDDEN, "A-004", "해당 예약 대기에 대해 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-005", "올바르지 않은 형식의 토큰입니다."),
    INVALID_BEARER(HttpStatus.UNAUTHORIZED, "A-006", "올바르지 않은 형식의 전달자입니다."),
    REQUIRE_TOKEN(HttpStatus.UNAUTHORIZED, "A-007", "토큰이 필요합니다."),
    REQUIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A-008", "해당 요청을 수행하려면 토큰이 필요합니다."),


    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E-000", "서버의 에러입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
