package nextstep;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import auth.UserDetailsService;
import lombok.RequiredArgsConstructor;
import nextstep.member.MemberDao;
import nextstep.member.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AuthConfiguration {

    private final MemberDao memberDao;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(userDetailsService(), jwtTokenProvider());
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MemberService(memberDao);
    }
}
