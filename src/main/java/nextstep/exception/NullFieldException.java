package nextstep.exception;

public class NullFieldException extends RuntimeException{
    public NullFieldException(String fieldName, Class<?> type) {
        super(String.format("%s의 %s는 null일 수 없습니다.", type.getSimpleName(), fieldName));
    }
}
