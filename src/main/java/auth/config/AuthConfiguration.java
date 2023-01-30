package auth.config;

import auth.controller.AuthController;
import auth.dao.MemberRoleDao;
import auth.service.AuthService;
import auth.support.AuthenticationInterceptor;
import auth.support.AuthenticationPrincipalArgumentResolver;
import auth.support.AuthorizationInterceptor;
import auth.support.JwtTokenProvider;
import nextstep.member.dao.MemberDao;
import nextstep.member.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@PropertySource("classpath:application.yml")
public class AuthConfiguration {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @Bean
    public JwtTokenProvider jwtTokenProvider(){
        return new JwtTokenProvider(secretKey, validityInMilliseconds);
    }

    @Bean
    public MemberRoleDao memberRoleDao(JdbcTemplate jdbcTemplate){
        return new MemberRoleDao(jdbcTemplate);
    }

    @Bean
    public AuthService authService(MemberRoleDao memberRoleDao, MemberDao memberDao, JwtTokenProvider jwtTokenProvider){
        return new AuthService(jwtTokenProvider, memberDao, memberRoleDao);
    }

    @Bean
    public AuthController authController(AuthService authService){
        return new AuthController(authService);
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberService memberService){
        return new AuthenticationPrincipalArgumentResolver(jwtTokenProvider, memberService);
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor(JwtTokenProvider jwtTokenProvider){
        return new AuthenticationInterceptor(jwtTokenProvider);
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor(JwtTokenProvider jwtTokenProvider, AuthService authService){
        return new AuthorizationInterceptor(jwtTokenProvider, authService);
    }
}
