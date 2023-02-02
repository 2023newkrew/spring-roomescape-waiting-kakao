package nextstep.config;

import auth.*;
import auth.userauth.UserAuthRepository;
import auth.userauth.UserAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public JwtTokenProvider jwtTokenProvider(){
        return new JwtTokenProvider();
    }

    @Bean
    public UserAuthService userAuthService(UserAuthRepository userAuthRepository) {
        return new UserAuthService(userAuthRepository);
    }

    @Bean
    public UserAuthRepository userAuthRepository(){
        return new UserAuthRepository();
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(userAuthService(userAuthRepository()), jwtTokenProvider());
    }
    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }
}
