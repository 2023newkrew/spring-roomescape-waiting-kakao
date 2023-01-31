package nextstep.support;

public class NoEntityException extends RuntimeException {
    private static final String MESSAGE = "조회할 수 없습니다.";

    public NoEntityException() {
        super(MESSAGE);
    }
}
