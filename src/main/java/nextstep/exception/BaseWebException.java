package nextstep.exception;

public class BaseWebException extends RuntimeException {

    private final int status;

    private final String type;

    private BaseWebException(String message, int status, String type) {
        super(message);
        this.type = type;
        this.status = status;
    }

    public BaseWebException(String expected, String actual, String context, int status, String type) {
        this(makeMessage(expected, actual, context), status, type);
    }

    public String getFullMessage() {
        return super.getMessage() + "CLASS: " + type + '\n';
    }

    public int getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    private static String makeMessage(String expected, String actual, String context) {
        return "\n" + "EXPECTED: " + expected + '\n' + "ACTUAL: " + actual + '\n' + "CONTEXT: " + context + '\n';
    }
}
