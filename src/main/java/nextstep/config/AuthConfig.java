package nextstep.config;

import auth.controller.LoginController;
import auth.repository.UserDetailsRepository;
import auth.repository.UserDetailsRepositoryImpl;
import auth.service.LoginService;
import auth.support.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public JwtTokenProvider jwtTokenProvider() { return new JwtTokenProvider(); }

    @Bean
    public LoginController loginController() { return new LoginController(loginService()); }

    @Bean
    public UserDetailsRepository userDetailsRepository() {
        return new UserDetailsRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public LoginService loginService() { return new LoginService(userDetailsRepository(), jwtTokenProvider()); }
}
