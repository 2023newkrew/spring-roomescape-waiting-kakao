package nextstep.config;

import auth.controller.LoginController;
import auth.repository.UserDetailsDao;
import auth.service.LoginService;
import auth.support.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, validityInMilliseconds);
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }

    @Bean
    public UserDetailsDao userDetailsRepository() {
        return new UserDetailsDao(jdbcTemplate);
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(userDetailsRepository(), jwtTokenProvider());
    }
}
