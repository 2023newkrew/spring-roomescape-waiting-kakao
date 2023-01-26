package app.auth;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private LoginDao loginDao;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(LoginDao loginDao, JwtTokenProvider jwtTokenProvider) {
        this.loginDao = loginDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetail userDetail = loginDao.findByUsername(tokenRequest.getUsername())
                .orElseThrow(() -> new AuthenticationException());

        if (userDetail.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(userDetail.getId() + "", userDetail.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetail extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return loginDao.findById(id)
                .orElseThrow(() -> new AuthenticationException());
    }
}
