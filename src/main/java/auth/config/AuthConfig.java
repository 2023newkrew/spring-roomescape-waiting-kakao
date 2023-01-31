package auth.config;

import auth.LoginController;
import auth.LoginService;
import auth.utils.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
    private final LoginMemberDao loginMemberDao;

    public AuthConfig(LoginMemberDao loginMemberDao) {
        this.loginMemberDao = loginMemberDao;
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
        return new LoginService(jwtTokenProvider(), loginMemberDao);
    }

    @Bean
    public WebMvcAuthConfiguration webMvcAuthConfiguration(){
        return new WebMvcAuthConfiguration(jwtTokenProvider(), loginService());
    }

}
