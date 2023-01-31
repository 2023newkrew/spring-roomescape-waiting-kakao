package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import roomescape.controller.errors.ErrorCode;
import roomescape.service.exception.ServiceException;

import java.util.Date;

@Service
public class JWTProvider {
    private static final String SECRET_KEY = "learning-test-spring";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;

    public Jws<Claims> parse(String jwtToken) {
        try {
            return Jwts.parser()
                       .setSigningKey(SECRET_KEY)
                       .parseClaimsJws(jwtToken);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INVALID_TOKEN);
        }
    }

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
}
