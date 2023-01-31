package nextstep;

import auth.*;
import lombok.RequiredArgsConstructor;
import nextstep.member.MemberDao;
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
    private final MemberDao memberDao;

    private final UserAuthenticator userAuthenticator;

    @Bean
    public UserAuthenticator userValidator() {
        return new UserAuthenticatorImpl(memberDao);
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
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService()));
    }
}
