package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import roomescape.exception.AuthorizationException;

import java.util.Date;

@Service
public class JWTProvider {
    private static final String SECRET_KEY = "learning-test-spring";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;

    public String createToken(String subject) {
        Claims claims = Jwts.claims().setSubject(subject);
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);

        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(validity)
                   .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                   .compact();
    }

    public String getSubject(String token) {
        var claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        if (claims.getBody().getExpiration().before(new Date())) {
            throw new AuthorizationException();
        }
        return claims.getBody().getSubject();
    }
}
