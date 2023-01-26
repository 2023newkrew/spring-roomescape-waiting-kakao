package nextstep.config;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import auth.UserDetailsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@Configuration
public class AuthConfiguration {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public LoginController loginController(LoginService loginService) {
        @RestController
        class ControllerBean extends LoginController {
            ControllerBean(LoginService loginService) {
                super(loginService);
            }
        }
        return new ControllerBean(loginService);
    }

    @Bean
    public LoginService loginService(UserDetailsRepository userDetailsRepository) {
        return new LoginService(userDetailsRepository, jwtTokenProvider());
    }
}
