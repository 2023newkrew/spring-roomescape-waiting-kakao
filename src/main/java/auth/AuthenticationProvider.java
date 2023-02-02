package auth;

public class AuthenticationProvider {
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(UserDetails userDetails) {
        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());
        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }
}
