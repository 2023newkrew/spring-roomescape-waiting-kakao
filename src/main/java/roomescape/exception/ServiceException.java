package roomescape.exception;

import errors.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServiceException extends RuntimeException {
    private final ErrorCode errorCode;
}
