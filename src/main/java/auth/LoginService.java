package auth;

import auth.exception.AuthErrorCode;
import auth.exception.AuthException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginService {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userDetailsService.findByUsername(tokenRequest.getUsername());
        if (userDetails == null || userDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthException(AuthErrorCode.LOGIN_FAILED_WRONG_USERNAME_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createToken(String.valueOf(userDetails.getId()), userDetails.getRole());

        return new TokenResponse(accessToken);
    }

    public UserDetails extractUserDetails(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return userDetailsService.findById(id);
    }
}
