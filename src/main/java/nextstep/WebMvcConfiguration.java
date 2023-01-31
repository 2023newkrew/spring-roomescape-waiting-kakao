package nextstep;

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
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final MemberService memberService;

    private final UserAuthenticator userAuthenticator;

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtTokenProvider(), userAuthenticator, tokenExtractor()))
                .order(1)
                .addPathPatterns("/admin/**", "/reservations/**", "/reservation-waitings/**");
        registry.addInterceptor(new AdminInterceptor())
                .order(2)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService(), tokenExtractor()));
    }
}
