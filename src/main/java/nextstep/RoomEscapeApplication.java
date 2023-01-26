package nextstep;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import nextstep.member.MemberDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RoomEscapeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomEscapeApplication.class, args);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey,
                                             @Value("${security.jwt.token.expire-length}") long validityInMilliseconds) {
        return new JwtTokenProvider(secretKey, validityInMilliseconds);
    }

    @Bean
    public LoginService loginService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        return new LoginService(memberDao, jwtTokenProvider);
    }

    @Bean
    public LoginController loginController(LoginService loginService) {
        return new LoginController(loginService);
    }
}
