package roomescape.nextstep.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final int status;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this(errorCode.getHttpStatus()
                .value(), errorCode.getMessage());
    }
}
