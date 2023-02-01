package auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class AuthConfiguration {

    @Bean
    public JwtTokenProvider jwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey,
                                             @Value("${security.jwt.token.expire-length}") long validityInMilliseconds) {
        return new JwtTokenProvider(secretKey, validityInMilliseconds);
    }

    @Bean
    public LoginController loginController(UserDetailsService<?> userDetailsService) {
        return new LoginController(userDetailsService);
    }
}
