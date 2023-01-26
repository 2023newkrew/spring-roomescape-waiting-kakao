package auth.provider;

import auth.domain.TokenData;
import io.jsonwebtoken.*;
import nextstep.etc.exception.AuthenticationException;
import nextstep.etc.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(TokenData tokenData) {
        Claims claims = Jwts.claims();
        claims.put("id", tokenData.getId());
        claims.put("role", tokenData.getRole());
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public TokenData getTokenData(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return new TokenData(claims.get("id", Long.class), claims.get("role", String.class));
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        }
        catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getValidToken(String bearerToken) {
        if (isNullOrNotBearer(bearerToken)) {
            throw new AuthenticationException(ErrorMessage.NOT_LOGGED_IN);
        }

        var accessToken = bearerToken.substring(7);

        if (!validateToken(accessToken)) {
            throw new AuthenticationException(ErrorMessage.INVALID_TOKEN);
        }

        return accessToken;

    }

    private boolean isNullOrNotBearer(String bearerToken) {
        return bearerToken == null || !bearerToken.startsWith("Bearer ");
    }
}
