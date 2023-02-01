package app.nextstep.config;

import app.auth.util.JwtTokenProvider;
import app.auth.controller.LoginController;
import app.auth.dao.LoginDao;
import app.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@PropertySource("classpath:application.properties")
public class AuthConfig {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @Bean
    public JwtTokenProvider jwtTokenProvider(){
        return new JwtTokenProvider(secretKey, validityInMilliseconds);
    }

    @Bean
    public LoginDao loginDao(JdbcTemplate jdbcTemplate){
        return new LoginDao(jdbcTemplate);
    }

    @Bean
    public LoginService loginService(LoginDao loginDao, JwtTokenProvider jwtTokenProvider){
        return new LoginService(loginDao, jwtTokenProvider);
    }

    @Bean
    public LoginController loginController(LoginService loginService){
        return new LoginController(loginService);
    }
}
