package auth.service;

import auth.UserAuthenticator;
import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.entity.UserDetails;
import auth.exception.AuthExceptionCode;
import auth.exception.AuthenticationException;
import auth.jwt.JwtTokenProvider;
import auth.jwt.TokenExtractor;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthenticator userValidator;
    private final TokenExtractor tokenExtractor;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = userValidator.authenticate(tokenRequest.getUsername(), tokenRequest.getPassword())
                .orElseThrow(() -> new AuthenticationException(AuthExceptionCode.AUTHENTICATION_FAIL));
        String accessToken = jwtTokenProvider.createToken(userDetails.getId().toString());

        return new TokenResponse(accessToken);
    }

    public boolean isAuthorizedHeader(String authorizationHeader) {
        Optional<String> credential = tokenExtractor.extractToken(authorizationHeader);
        return credential.isPresent() && jwtTokenProvider.validateToken(credential.get());
    }

    public Optional<Long> extractMemberId(String authorizationHeader) {
        Optional<String> credential = tokenExtractor.extractToken(authorizationHeader);
        try {
            return Optional.of(Long.parseLong(jwtTokenProvider.getPrincipal(credential.get())));
        } catch (NoSuchElementException | NumberFormatException e) {
            return Optional.empty();
        }
    }

    public boolean isAdmin(String authorizationHeader) {
        Optional<Long> memberId = extractMemberId(authorizationHeader);
        return memberId.filter(userValidator::isAdmin).isPresent();
    }

}
