package auth;

import auth.dto.TokenRequest;

public interface UserChecker {
    UserDetails check(TokenRequest tokenRequest);
}
