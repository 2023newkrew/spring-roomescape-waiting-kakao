package auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationProvider authenticationProvider;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = authenticationProvider.findByUsername(tokenRequest.getUsername());
        if (userDetails == null || userDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());
        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractUserDetails(String credential) {
        return authenticationProvider.findById(extractPrincipal(credential));
    }
}
