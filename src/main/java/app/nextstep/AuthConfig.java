package app.nextstep;

import app.auth.JwtTokenProvider;
import app.auth.LoginController;
import app.auth.LoginDao;
import app.auth.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

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
