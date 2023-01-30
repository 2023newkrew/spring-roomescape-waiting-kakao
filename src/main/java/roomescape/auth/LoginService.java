package roomescape.auth;

import org.springframework.stereotype.Service;

public class LoginService {

    private final UserDetailsDao userDetailsDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(UserDetailsDao userDetailsDao, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsDao = userDetailsDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails member = userDetailsDao.findByUsername2(tokenRequest.getUsername());

        if (member == null || member.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(member.getUsername() + "", member.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractMember(String credential) {
        return userDetailsDao.findByUsername2(jwtTokenProvider.getPrincipal(credential));
    }
}
