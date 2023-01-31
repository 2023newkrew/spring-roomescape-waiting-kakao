package nextstep.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum RoomEscapeExceptionCode {
    MEMBER_NOT_FOUND(NOT_FOUND, "멤버를 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND(NOT_FOUND, "예약을 찾을 수 없습니다."),
    NOT_OWN_RESERVATION(BAD_REQUEST, "본인의 예약이 아닙니다."),
    RESERVATION_WAITING_NOT_FOUND(NOT_FOUND, "예약대기를 찾을 수 없습니다."),
    NOT_OWN_RESERVATION_WAITING(BAD_REQUEST, "본인의 예약대기가 아닙니다."),
    SCHEDULE_NOT_FOUND(NOT_FOUND, "스케줄을 찾을 수 없습니다."),
    RESERVED_SCHEDULE(BAD_REQUEST, "해당 스케줄이 이미 예약 돼있습니다."),
    THEME_NOT_FOUND(NOT_FOUND, "테마를 찾을 수 없습니다."),
    UNEXPECTED_EXCEPTION(INTERNAL_SERVER_ERROR, "서버 오류로 요청을 처리할 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
