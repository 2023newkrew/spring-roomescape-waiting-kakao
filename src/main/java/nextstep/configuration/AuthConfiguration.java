package nextstep.configuration;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import nextstep.member.MemberDao;
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
    public LoginService loginService(MemberDao memberDao) {
        return new LoginService(memberDao, jwtTokenProvider());
    }
}
