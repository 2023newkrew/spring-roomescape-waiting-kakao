package nextstep.config;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import auth.UserDetailService;
import nextstep.member.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public JwtTokenProvider jwtTokenProvider(){
        return new JwtTokenProvider();
    }

    @Bean
    public UserDetailService userDetailService(MemberDao memberDao) {
        return new UserDetailService(memberDao);
    }

    @Bean
    public LoginService loginService(MemberDao memberDao) {
        return new LoginService(userDetailService(memberDao), jwtTokenProvider());
    }
    @Bean
    public LoginController loginController(MemberDao memberDao) {
        return new LoginController(loginService(memberDao));
    }
}
