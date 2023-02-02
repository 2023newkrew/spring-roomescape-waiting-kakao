package auth;

import nextstep.support.NotExistEntityException;

public class LoginService {
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userDao.findByUsername(tokenRequest.getUsername())
                .orElseThrow(AuthenticationException::new);
        if (userDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return userDao.findById(id)
                .orElseThrow(NotExistEntityException::new);
    }
}
