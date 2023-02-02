package auth.config;

import auth.AuthenticateProvider;
import auth.JwtTokenProvider;
import auth.login.LoginController;
import auth.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AuthConfiguration {

    private final AuthenticateProvider authenticateProvider;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(jwtTokenProvider(), authenticateProvider);
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }
}
