package auth.jwt;

import java.util.Optional;

public class TokenExtractor {
    public Optional<String> extractToken(String tokenWithClass) {
        try {
            return Optional.of(tokenWithClass.split(" ")[1]);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }
}
