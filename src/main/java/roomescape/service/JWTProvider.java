package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class JWTProvider {
    private static final String SECRET_KEY = "learning-test-spring";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;
    private static final Pattern BEARER_TOKEN = Pattern.compile("Bearer *(.+)");

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

    public Optional<String> getTokenFromAuthHeader(String header) {
        if (header == null) {
            return Optional.empty();
        }
        var matcher = BEARER_TOKEN.matcher(header);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(matcher.group(1));
    }

    public boolean isInvalidJWT(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isExpired(String token) {
        var claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        return claims.getBody().getExpiration().before(new Date());
    }

    public String getSubject(String token) {
        var claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    public boolean getIsAdmin(String token) {
        var claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
        return claims.getBody().get("is_admin", Boolean.class);
    }
}
