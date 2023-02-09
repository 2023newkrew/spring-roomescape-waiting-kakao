package nextstep;

import auth.controller.LoginController;
import auth.dao.UserDetailsDao;
import auth.service.LoginService;
import auth.support.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {

    private UserDetailsDao userDetailsDao;

    public AuthConfiguration(UserDetailsDao userDetailsDao) {
        this.userDetailsDao = userDetailsDao;
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public LoginService loginService(JwtTokenProvider jwtTokenProvider) {
        return new LoginService(userDetailsDao, jwtTokenProvider);
    }

    @Bean
    public LoginController loginController(LoginService loginService) {
        return new LoginController(loginService);
    }
}
