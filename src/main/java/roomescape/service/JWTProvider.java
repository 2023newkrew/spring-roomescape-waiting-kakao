package roomescape.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.controller.errors.ErrorCode;
import roomescape.service.exception.ServiceException;

import java.util.Date;

@Service
public class JWTProvider {

    @Value("${jwt.secret.symmetric-key}")
    @Getter
    private String secretKey;

    @Value("${jwt.secret.validity-in-milliseconds}")
    @Getter
    private long validityInMilliseconds;

    public Jws<Claims> parse(String jwtToken) {
        try {
            return Jwts.parser()
                       .setSigningKey(secretKey)
                       .parseClaimsJws(jwtToken);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String createToken(String subject, boolean isAdmin) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("is_admin", isAdmin);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(validity)
                   .signWith(SignatureAlgorithm.HS256, secretKey)
                   .compact();
    }
}
