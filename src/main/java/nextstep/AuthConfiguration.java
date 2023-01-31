package nextstep;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import auth.MemberDetailsService;
import nextstep.member.MemberDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AuthConfiguration {

    private final JdbcTemplate jdbcTemplate;

    public AuthConfiguration(JdbcTemplate jdbcTemplate) {
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
    public LoginService loginService() {
        return new LoginService(memberDetailsService(), jwtTokenProvider());
    }

    @Bean
    public MemberDetailsService memberDetailsService() {
        return new MemberDetailsServiceImpl(jdbcTemplate);
    }
}
