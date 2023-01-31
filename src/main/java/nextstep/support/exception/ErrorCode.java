package nextstep.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    DUPLICATE_RESERVATION("중복된 예약입니다.", 2001, HttpStatus.BAD_REQUEST),
    NO_SUCH_THEME("없는 테마입니다.", 2002, HttpStatus.NOT_FOUND),
    NO_SUCH_RESERVATION("없는 예약입니다.", 2003, HttpStatus.NOT_FOUND),
    NO_SUCH_SCHEDULE("없는 스케줄입니다.", 2004, HttpStatus.NOT_FOUND),
    NO_SUCH_WAITING("없는 대기열입니다.", 2005, HttpStatus.NOT_FOUND),
    NOT_WAITING_OWNER("본인의 대기가 아닙니다.", 2006, HttpStatus.UNAUTHORIZED),
    NOT_RESERVATION_OWNER("본인의 예약이 아닙니다.", 2007, HttpStatus.UNAUTHORIZED),
    ILLEGAL_APPROVE_ATTEMPT("예약 대기 상태가 아닙니다.", 2008, HttpStatus.BAD_REQUEST);

    private final String message;
    private final int errorCode;
    private final HttpStatus httpStatus;
}
