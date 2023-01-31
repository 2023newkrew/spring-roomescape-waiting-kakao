package nextstep;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import auth.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class AuthConfiguration {

    private final JdbcTemplate jdbcTemplate;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(userDetailsService(), jwtTokenProvider());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService(jdbcTemplate);
    }

}
