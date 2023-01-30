package nextstep.exception;

public class BaseWebException extends RuntimeException {

    int status;
    Class<?> type;

    private BaseWebException(String message, int status, Class<?> type) {
        super(message);
        this.type = type;
        this.status = status;
    }

    public BaseWebException(String expected, String actual, String context, int status, Class<?> type) {
        this(makeMessage(expected, actual, context), status, type);
    }

    private static String makeMessage(String expected, String actual, String context) {
        return "\n"
                + "EXPECTED: " + expected + '\n'
                + "ACTUAL: " + actual + '\n'
                + "CONTEXT: " + context + '\n';
    }

    public String getFullMessage(){
        return super.getMessage() + "CLASS: " + type.getName() + '\n';
    }

}
