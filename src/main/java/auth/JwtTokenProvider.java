package auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;

public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(final String principal, final String role) {
        Claims claims = Jwts.claims()
            .setSubject(principal);
        Long validityInSeconds = validityInMilliseconds / 1000;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity = now.plusSeconds(validityInSeconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Timestamp.valueOf(now))
            .setExpiration(Timestamp.valueOf(validity))
            .claim("role", role)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public String getPrincipal(final String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public String getRole(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            LocalDateTime now = LocalDateTime.now();
            Jws<Claims> claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);

            return !claims.getBody()
                .getExpiration()
                .before(Timestamp.valueOf(now));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
