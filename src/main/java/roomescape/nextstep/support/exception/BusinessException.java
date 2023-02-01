package roomescape.nextstep.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
}
