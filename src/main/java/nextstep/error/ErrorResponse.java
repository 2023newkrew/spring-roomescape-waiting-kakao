package nextstep.error;

public class ErrorResponse {
    private int status;
    private String message;

    protected ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getStatus(), errorCode.getMessage());
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

