package nextstep.config;

import auth.domain.JwtTokenProvider;
import auth.domain.userdetails.UserDetailsRepository;
import auth.presentation.LoginController;
import auth.service.LoginService;
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
