package auth;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException() { super("접근 권한이 없습니다."); }
}
