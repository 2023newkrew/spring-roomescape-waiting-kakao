package auth;

import auth.dto.TokenRequest;

public interface UserChecker {
    UserDetails userCheck(TokenRequest tokenRequest);
}
