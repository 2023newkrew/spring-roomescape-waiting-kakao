package nextstep.config;

import auth.support.JwtTokenProvider;
import auth.presentation.LoginController;
import auth.service.LoginService;
import auth.domain.UserDetailsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
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
