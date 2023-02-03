package nextstep.exception;

public class InvalidFieldException extends IllegalArgumentException{
    public InvalidFieldException(String value, Class<?> type) {
        super(String.format("%s는 %s의 값을 가질 수 없습니다.", type.getSimpleName(), value));
    }
}
