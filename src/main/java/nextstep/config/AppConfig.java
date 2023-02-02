package nextstep.config;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import nextstep.member.UserDetailsDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * auth패키지의 LoginController를 nextstep에서 사용 하기 위해 Bean을 등록하는 클래스
 */
@Configuration
public class AppConfig {
    @Bean
    public JwtTokenProvider jwtTokenProvider(){
        return new JwtTokenProvider();
    }

    @Bean
    public LoginService loginService(UserDetailsDao userDetailsDao) {
        return new LoginService(jwtTokenProvider(), userDetailsDao);
    }

    @Bean
    public LoginController loginController(UserDetailsDao userDetailsDao) {
        return new LoginController(loginService(userDetailsDao));
    }
}
