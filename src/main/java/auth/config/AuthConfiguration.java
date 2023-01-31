package auth.config;

import auth.login.LoginController;
import auth.login.LoginService;
import auth.login.MemberDao;
import auth.resolver.LoginMemberArgumentResolver;
import auth.util.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {
    private final MemberDao memberDao;

    public AuthConfiguration(MemberDao memberDao) {
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
