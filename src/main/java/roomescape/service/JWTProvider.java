package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import roomescape.exception.AuthorizationException;

import java.util.Arrays;
import java.util.Date;

@Service
public class JWTProvider {
    private static final String SECRET_KEY = "learning-test-spring";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;

    public String createToken(String subject, boolean isAdmin) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("is_admin", isAdmin);
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

    public boolean getIsAdmin(String token) {
        var claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        if (claims.getBody().getExpiration().before(new Date())) {
            throw new AuthorizationException();
        }
        System.out.println(Arrays.toString(claims.getBody().keySet().toArray()));
        return claims.getBody().get("is_admin", Boolean.class);
    }
}
