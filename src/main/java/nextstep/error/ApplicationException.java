package nextstep.error;

public class ApplicationException extends RuntimeException {

    private ErrorType errorType;

    public ApplicationException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ApplicationException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
