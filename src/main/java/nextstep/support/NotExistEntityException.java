package nextstep.support;

public class NotExistEntityException extends RuntimeException {
    public NotExistEntityException() {
        super("대상을 찾을 수 없습니다.");
    }
}

