package auth;

import org.springframework.stereotype.Service;

//@Service
public class LoginService {
    private AuthenticationProvider authenticationProvider;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(AuthenticationProvider authenticationProvider, JwtTokenProvider jwtTokenProvider) {
        this.authenticationProvider = authenticationProvider;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = authenticationProvider.getUserDetails(tokenRequest);
        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        String role = jwtTokenProvider.getRole(credential);
        return new UserDetails(id, role);
    }
}
