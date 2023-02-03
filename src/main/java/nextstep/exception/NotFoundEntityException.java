package nextstep.exception;

public class NotFoundEntityException extends RuntimeException {

    public NotFoundEntityException() {
        super("존재하지 않는 엔티티입니다.");
    }
}
