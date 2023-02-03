package nextstep.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class RoomEscapeException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public RoomEscapeException(RoomEscapeExceptionCode roomEscapeExceptionCode) {
        super(roomEscapeExceptionCode.getMessage());

        this.httpStatus = roomEscapeExceptionCode.getHttpStatus();
        this.message = roomEscapeExceptionCode.getMessage();
    }
}
