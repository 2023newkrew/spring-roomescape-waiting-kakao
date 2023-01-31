package nextstep.config;

import auth.controller.LoginController;
import auth.repository.UserDetailsRepository;
import auth.repository.UserDetailsRepositoryImpl;
import auth.service.LoginService;
import auth.support.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "security.jwt.token")
public class AuthConfig {
    private String secretKey;
    private Long expireLength;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, expireLength);
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }

    @Bean
    public UserDetailsRepository userDetailsRepository() {
        return new UserDetailsRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(userDetailsRepository(), jwtTokenProvider());
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setExpireLength(Long expireLength) {
        this.expireLength = expireLength;
    }
}
