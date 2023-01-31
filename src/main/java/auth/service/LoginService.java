package auth.service;

import auth.UserAuthenticator;
import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.entity.UserDetails;
import auth.exception.AuthExceptionCode;
import auth.exception.AuthenticationException;
import auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthenticator userValidator;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userValidator.authenticate(tokenRequest.getUsername(), tokenRequest.getPassword())
                .orElseThrow(() -> new AuthenticationException(AuthExceptionCode.AUTHENTICATION_FAIL));
        String accessToken = jwtTokenProvider.createToken(userDetails.getId().toString());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }
}
