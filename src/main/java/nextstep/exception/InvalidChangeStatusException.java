package nextstep.exception;

public class InvalidChangeStatusException extends IllegalStateException {

    public InvalidChangeStatusException(String before, String after, String typeName) {
        super(String.format("%s는 %s로 변환할 수 없습니다. \n WHERE: %s\n", before, after, typeName));
    }
}
