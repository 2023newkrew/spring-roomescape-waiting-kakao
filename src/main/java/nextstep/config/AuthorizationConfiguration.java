package nextstep.config;

import auth.login.LoginController;
import auth.login.LoginService;
import auth.login.MemberDao;
import auth.resolver.LoginMemberArgumentResolver;
import auth.util.JwtTokenProvider;
import nextstep.web.member.domain.Member;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorizationConfiguration {

    private final MemberDao<Member> memberDao;

    public AuthorizationConfiguration(MemberDao<Member> memberDao) {
        this.memberDao = memberDao;
    }

    @Bean
    public LoginService<Member> loginService() {
        return new LoginService<>(memberDao, jwtTokenProvider());
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }

    @Bean
    public LoginMemberArgumentResolver loginMemberArgumentResolver(){
        return new LoginMemberArgumentResolver(loginService());
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }
}
