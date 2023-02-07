package errors;

public record ErrorResponse(int status, String code, String message) {
    public static ErrorResponse from(ErrorCode code) {
        return new ErrorResponse(
                code.getStatus().value(),
                code.getCode(),
                code.getMessage()
        );
    }
}
