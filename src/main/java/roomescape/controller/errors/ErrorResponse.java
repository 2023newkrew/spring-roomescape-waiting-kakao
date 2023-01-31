package roomescape.controller.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String code;
    private final String message;

    public static ErrorResponse from(ErrorCode code) {

        return new ErrorResponse(
                code.getStatus().value(),
                code.getCode(),
                code.getMessage()
        );
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
               "status=" + status +
               ", code='" + code + '\'' +
               ", message='" + message + '\'' +
               '}';
    }
}
