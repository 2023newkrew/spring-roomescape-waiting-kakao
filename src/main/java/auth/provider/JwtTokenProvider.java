package auth.provider;

import auth.domain.UserDetails;
import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import auth.exception.AuthenticationException;
import auth.exception.ErrorMessage;
import auth.service.AuthenticationPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;
import java.util.Objects;

@RequiredArgsConstructor
public class JwtTokenProvider {

    private final String secretKey;

    private final long validityInMilliseconds;

    private final AuthenticationPrincipal principal;

    public TokenResponse createToken(TokenRequest request) {
        UserDetails userDetails = principal.getByUsername(request.getUsername());
        validatePassword(userDetails, request.getPassword());

        return new TokenResponse(createToken(userDetails));
    }

    private void validatePassword(UserDetails userDetails, String password) {
        if (Objects.isNull(userDetails) || userDetails.isWrongPassword(password)) {
            throw new AuthenticationException(ErrorMessage.INVALID_USERNAME_OR_PASSWORD);
        }
    }

    private String createToken(UserDetails userDetails) {
        Claims claims = Jwts.claims();
        claims.put("username", userDetails.getUsername());
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public UserDetails getUserDetails(String bearerToken) {
        Claims body = getValidBody(bearerToken);

        return principal.getByUsername(body.get("username", String.class));
    }

    private Claims getValidBody(String bearerToken) {
        if (isBlankOrNotBearer(bearerToken)) {
            throw new AuthenticationException(ErrorMessage.NOT_LOGGED_IN);
        }
        String accessToken = bearerToken.substring(7);

        return parseBody(accessToken);
    }

    private boolean isBlankOrNotBearer(String bearerToken) {
        return Strings.isBlank(bearerToken) || !bearerToken.startsWith("Bearer ");
    }

    private Claims parseBody(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(accessToken)
                    .getBody();
        }
        catch (ExpiredJwtException e) {
            throw new AuthenticationException(ErrorMessage.EXPIRED_TOKEN);
        }
        catch (Exception e) {
            throw new AuthenticationException(ErrorMessage.INVALID_TOKEN);
        }
    }

    public void validateAdmin(UserDetails userDetails) {
        if (Objects.isNull(userDetails)) {
            throw new AuthenticationException(ErrorMessage.NOT_LOGGED_IN);
        }
        if (userDetails.isNotAdmin()) {
            throw new AuthenticationException(ErrorMessage.NOT_ADMIN);
        }
    }
}
