package auth.jwt;

import java.util.Optional;

public class TokenExtractor {
    public Optional<String> extractToken(String tokenWithClass) {
        String token;
        try {
            token = tokenWithClass.split(" ")[1];
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(token);
    }
}
