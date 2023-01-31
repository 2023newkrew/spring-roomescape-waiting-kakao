package auth.support;

import auth.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenProvider {
    private final String secretKey;
    private final Long validityInMilliseconds;

    public JwtTokenProvider(String secretKey, Long validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

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
