package roomescape.auth;


public class LoginService {

    private final UserDetailsDao userDetailsDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(UserDetailsDao userDetailsDao, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsDao = userDetailsDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails member = userDetailsDao.findByUsername(tokenRequest.username());

        if (member == null || member.checkWrongPassword(tokenRequest.password())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(member.getUsername() + "", member.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractMember(String credential) {
        return userDetailsDao.findByUsername(jwtTokenProvider.getPrincipal(credential));
    }
}
