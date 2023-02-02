package auth.login;

import auth.AuthenticateProvider;
import auth.JwtTokenProvider;
import auth.UserDetails;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticateProvider authenticateProvider;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = authenticateProvider.createUserDetails(tokenRequest.getUsername(), tokenRequest.getPassword());

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public UserDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        String role = jwtTokenProvider.getRole(credential);
        return authenticateProvider.createUserDetails(id, role);
    }
}
