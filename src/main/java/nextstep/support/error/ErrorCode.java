package nextstep.support.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    NOT_ADMIN_ROLE(HttpStatus.FORBIDDEN, "관리자 권한이 없습니다."),
    ALREADY_RESERVED_SCHEDULE(HttpStatus.BAD_REQUEST, "이미 자신의 예약이 존재하는 스케줄에는 예약 대기를 할 수 없습니다."),
    DUPLICATE_RESERVATION(HttpStatus.BAD_REQUEST, "동일한 스케줄에 이미 예약이 존재합니다."),
    DUPLICATE_RESERVATION_WAITING(HttpStatus.BAD_REQUEST, "동일한 스케줄에 이미 예약 대기가 존재합니다."),
    NON_EXIST_MEMBER(HttpStatus.UNAUTHORIZED, "존재하지 않는 회원 정보입니다."),
    NON_EXIST_RESERVATION(HttpStatus.BAD_REQUEST, "존재하지 않는 예약입니다."),
    NON_EXIST_RESERVATION_WAITING(HttpStatus.BAD_REQUEST, "존재하지 않는 예약 대기입니다."),
    NOT_OWN_RESERVATION_WAITING(HttpStatus.FORBIDDEN, "자신의 예약 대기가 아닙니다.");
    private final HttpStatus status;
    private final String message;
}
