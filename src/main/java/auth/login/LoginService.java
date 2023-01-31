package auth.login;

import auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsFactory userDetailsFactory;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userDetailsFactory.makeUserDetails(tokenRequest.getUsername(), tokenRequest.getPassword());

        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        String role = jwtTokenProvider.getRole(credential);
        return userDetailsFactory.convertToUserDetails(id, role);
    }
}
