package auth.provider;

import auth.domain.TokenData;
import auth.exception.AuthenticationException;
import auth.exception.ErrorMessage;
import io.jsonwebtoken.*;
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
        } catch (MalformedJwtException e){
            throw new AuthenticationException(ErrorMessage.MALFORMED_TOKEN);
        } catch (ExpiredJwtException e){
            throw new AuthenticationException(ErrorMessage.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e){
            throw new AuthenticationException(ErrorMessage.UNSUPPORTED_TOKEN);
        } catch (SignatureException e ){
            throw new AuthenticationException(ErrorMessage.NOT_SIGNATURE_TOKEN);
        } catch (IllegalArgumentException e){
            throw new AuthenticationException(ErrorMessage.INVALID_TOKEN);
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
