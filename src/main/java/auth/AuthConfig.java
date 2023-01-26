package auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
    private final MemberDao memberDao;

    public AuthConfig(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(memberDao, jwtTokenProvider());
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }

    @Bean
    public LoginMemberArgumentResolver loginMemberArgumentResolver(){
        return new LoginMemberArgumentResolver(loginService());
    }
}
