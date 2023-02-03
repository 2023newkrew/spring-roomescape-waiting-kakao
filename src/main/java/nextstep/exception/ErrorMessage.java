package nextstep.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {

    NOT_AUTHORIZED("권한을 확인해주세요."),
    NOT_OWN_RESERVATION("자신의 예약이 아닙니다."),
    NOT_OWN_RESERVATION_WAITING("자신의 예약 대기가 아닙니다."),
    CANNOT_MAKE_RESERVATION_WAITING("이미 예약된 스케줄에 예약 대기를 생성할 수 없습니다."),
    DUPLICATED_RESERVATION("중복된 예약입니다."),
    DUPLICATED_RESERVATION_WAITING("중복된 예약대기입니다."),
    NOT_EXIST_RESERVATION("예약을 찾을 수 없습니다."),
    NOT_EXIST_THEME("테마를 찾을 수 없습니다."),
    NOT_EXIST_SCHEDULE("스케줄을 찾을 수 없습니다."),
    NOT_EXIST_RESERVATION_WAITING("예약대기를 찾을 수 없습니다."),
    ;

    private final String message;
}
