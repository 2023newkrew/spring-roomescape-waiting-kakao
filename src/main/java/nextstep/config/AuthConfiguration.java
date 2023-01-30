package nextstep.config;

import auth.domain.JwtTokenProvider;
import auth.domain.userdetails.UserDetailsRepository;
import auth.presentation.LoginController;
import auth.service.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, validityInMilliseconds);
    }

    @Bean
    public LoginController loginController(LoginService loginService) {
        return new LoginController(loginService);
    }

    @Bean
    public LoginService loginService(UserDetailsRepository userDetailsRepository) {
        return new LoginService(userDetailsRepository, jwtTokenProvider());
    }
}
