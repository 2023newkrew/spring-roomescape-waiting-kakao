package nextstep;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import auth.UserChecker;
import nextstep.member.MemberDao;
import nextstep.member.UserCheckerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {
    private final MemberDao memberDao;

    public AuthConfiguration(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Bean
    public UserChecker userChecker() {
        return new UserCheckerImpl(memberDao);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(jwtTokenProvider(), userChecker());
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }
}
