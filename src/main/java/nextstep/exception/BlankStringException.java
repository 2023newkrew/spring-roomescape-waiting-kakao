package nextstep.exception;

public class BlankStringException extends NullFieldException{
    public BlankStringException(String fieldName, Class<?> type) {
        super(fieldName, type);
    }
}
