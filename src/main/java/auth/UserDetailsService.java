package auth;

import nextstep.member.Member;

public interface UserDetailsService {
    TokenResponse createToken(TokenRequest tokenRequest);

    Long extractPrincipal(String credential);

    Member extractMember(String credential);
}
