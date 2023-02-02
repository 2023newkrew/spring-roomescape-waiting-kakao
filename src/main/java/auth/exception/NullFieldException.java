package auth.exception;

public class NullFieldException extends RuntimeException{
    public NullFieldException() {
        super("아이디 혹은 역할이 비어있습니다.");
    }
}
