package auth;

import auth.controller.LoginController;
import auth.service.LoginService;
import auth.service.MemberDetailsService;
import auth.token.JwtTokenExtractor;
import auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AuthConfig {

    private final JdbcTemplate jdbcTemplate;

    public AuthConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    JwtTokenExtractor jwtTokenExtractor() {
        return new JwtTokenExtractor(jwtTokenProvider());
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(memberDetailsService(), jwtTokenProvider());
    }

    @Bean
    public MemberDetailsService memberDetailsService() {
        return new MemberDetailsService(jdbcTemplate);
    }
}
