package auth;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() { super("접근 권한이 없습니다."); }
}
