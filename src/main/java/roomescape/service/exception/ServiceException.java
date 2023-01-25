package roomescape.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import roomescape.controller.errors.ErrorCode;

@Getter
@RequiredArgsConstructor
public class ServiceException extends RuntimeException {
    private final ErrorCode errorCode;
}
