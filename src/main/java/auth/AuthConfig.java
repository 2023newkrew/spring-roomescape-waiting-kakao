package auth;

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
    WebMvcAuthConfiguration webMvcAuthConfiguration(){
        return new WebMvcAuthConfiguration(jwtTokenProvider(), loginService());
    }
}
