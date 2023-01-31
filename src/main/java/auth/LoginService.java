package auth;

public class LoginService {
    private UserDetailsDAO userDetailsDAO;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(UserDetailsDAO userDetailsDAO, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsDAO = userDetailsDAO;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userDetailsDAO.findUserByUsername(tokenRequest.getUsername());
        if (userDetails == null || userDetails.checkWrongPassword(tokenRequest.getPassword())) {
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
        return userDetailsDAO.findUserById(id);
    }
}
