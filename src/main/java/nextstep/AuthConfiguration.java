package nextstep;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import auth.UserDetailsDao;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

public class AuthConfiguration {
    private final Environment environment;
    private final UserDetailsDao userDetailsDao;

    public AuthConfiguration(Environment environment, UserDetailsDao userDetailsDao) {
        this.environment = environment;
        this.userDetailsDao = userDetailsDao;
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(
                environment.getProperty("security.jwt.token.secret-key"),
                Long.parseLong(environment.getProperty("security.jwt.token.expire-length"))
        );
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(userDetailsDao, jwtTokenProvider());
    }

    @Bean
    public LoginController loginController(LoginService loginService) {
        return new LoginController(loginService);
    }
}
