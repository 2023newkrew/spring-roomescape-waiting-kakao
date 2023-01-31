package nextstep.exceptions.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.exceptions.ErrorCode;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
}
