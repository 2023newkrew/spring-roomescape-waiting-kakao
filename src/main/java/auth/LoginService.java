package auth;

import auth.utils.AuthenticationProvider;
import auth.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationProvider authenticationProvider;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = authenticationProvider.getUserDetails(tokenRequest);
        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());
        return new TokenResponse(accessToken);
    }

    public UserDetails extractUserDetails(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        Role role = jwtTokenProvider.getRole(credential);
        return new UserDetails(id, role);
    }
}
