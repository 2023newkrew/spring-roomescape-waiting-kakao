package nextstep.error;

public class ApplicationException extends RuntimeException {

    private ErrorType errorType;
    private Object[] args;

    public ApplicationException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ApplicationException(ErrorType errorType, Object... args) {
        this.errorType = errorType;
        this.args = args;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Object[] getArgs() {
        return args;
    }
}
