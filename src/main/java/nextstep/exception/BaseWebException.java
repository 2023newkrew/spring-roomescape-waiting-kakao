package nextstep.exception;

import static nextstep.utils.ValidateUtils.getType;

public class BaseWebException extends RuntimeException {

    int status;
    Class<?> type;

    private BaseWebException(String message, int status) {
        super(message);
        this.type = getType();
        this.status = status;
    }

    public BaseWebException(String expected, String actual, String context, int status) {
        this(makeMessage(expected, actual, context), status);
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
