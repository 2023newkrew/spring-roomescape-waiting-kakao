package nextstep.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserValidator userValidator;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userValidator.validate(tokenRequest.getUsername(), tokenRequest.getPassword());

        if (userDetails == null) {
            throw new AuthenticationException();
        }
        String accessToken = jwtTokenProvider.createToken(userDetails.getId() + "", userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetails extractUserDetails(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        String role = jwtTokenProvider.getRole(credential);
        return new UserDetails(id, role);
    }

}
