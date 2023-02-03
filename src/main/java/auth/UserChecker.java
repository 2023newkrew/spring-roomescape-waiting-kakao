package auth;

public interface UserChecker {
    UserDetails userCheck(TokenRequest tokenRequest);
}
