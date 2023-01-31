package auth.exception;

public abstract class AuthWebException extends RuntimeException {

    private final String type;
    private final int status;

    public AuthWebException(String expected, String actual, String context, String type, int status) {
        super(makeMessage(expected, actual, context));
        this.type = type;
        this.status = status;
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

    public String getFullMessage() {
        return super.getMessage() + "CLASS: " + type + '\n';
    }

}
