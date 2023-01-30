package nextstep.auth.support;

import io.jsonwebtoken.*;
import nextstep.auth.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private Long validityInMilliseconds;

    public String createCredential(String subject) {
        Claims claims = makeClaims(subject);
        Date expiration = makeExpiration();
        Date issuedAt = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private static Claims makeClaims(String subject) {
        return Jwts.claims()
                .setSubject(subject);
    }

    private Date makeExpiration() {
        Date now = new Date();
        return new Date(now.getTime() + validityInMilliseconds);
    }

    public String getSubject(String token) {
        String credential = getCredential(token);
        return getClaims(credential)
                        .getBody()
                        .getSubject();
    }

    public String getCredential(String token) {
        try{
            return token.split(" ")[1];
        }catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public boolean isValidToken(String token){
        try{
            String credential = getCredential(token);
            return isValidCredential(credential);
        }catch (Exception e) {
            return false;
        }
    }

    private boolean isValidCredential(String credential) {
        try{
            Date expiration = getExpiration(credential);
            return !(expiration.before(new Date()));
        }catch (Exception e){
            return false;
        }
    }

    private Date getExpiration(String credential) {
        Claims claims = getClaims(credential).getBody();
        try{
            return claims.getExpiration();
        }catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    private Jws<Claims> getClaims(String credential) {
        try{
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(credential);
        }catch (Exception e) {
            throw new InvalidTokenException();
        }
    }
}
