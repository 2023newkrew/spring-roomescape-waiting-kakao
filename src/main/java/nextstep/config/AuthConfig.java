package nextstep.config;

import auth.UserAuthenticator;
import auth.argumentResolver.LoginMemberArgumentResolver;
import auth.controller.LoginController;
import auth.interceptor.AdminInterceptor;
import auth.interceptor.LoginInterceptor;
import auth.jwt.JwtTokenProvider;
import auth.jwt.TokenExtractor;
import auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import nextstep.member.MemberService;
import nextstep.member.UserAuthenticatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {
    private final MemberService memberService;

    @Bean
    public UserAuthenticator userValidator() {
        return new UserAuthenticatorImpl(memberService);
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(jwtTokenProvider(), userValidator());
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public TokenExtractor tokenExtractor() {
        return new TokenExtractor();
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor(jwtTokenProvider(), userValidator(), tokenExtractor());
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }

    @Bean
    public LoginMemberArgumentResolver loginMemberArgumentResolver() {
        return new LoginMemberArgumentResolver(loginService(), tokenExtractor());
    }
}
