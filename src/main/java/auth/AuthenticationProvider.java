package auth;

public class AuthenticationProvider {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(UserDetails userDetails, String password) {
        if (!userDetails.isPasswordCorrect(password)) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(String.valueOf(userDetails.getId()), userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String token) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(token));
    }
}
