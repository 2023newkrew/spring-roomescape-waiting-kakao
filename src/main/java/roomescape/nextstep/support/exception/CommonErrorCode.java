package roomescape.nextstep.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {

    DUPLICATE_ENTITY(HttpStatus.BAD_REQUEST, "중복된 항목이 존재합니다.");

    private final HttpStatus httpStatus;

    private final String message;
}
