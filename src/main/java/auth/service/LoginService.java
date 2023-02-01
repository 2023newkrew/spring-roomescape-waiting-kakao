package auth.service;

import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.UserChecker;
import auth.UserDetails;
import auth.support.JwtTokenProvider;

public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserChecker userChecker;

    public LoginService(JwtTokenProvider jwtTokenProvider, UserChecker userChecker) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userChecker = userChecker;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userChecker.check(tokenRequest);

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public Long extractMember(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }
}
