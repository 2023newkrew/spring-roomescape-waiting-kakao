package nextstep.config;

import auth.UserAuthenticator;
import auth.argumentResolver.LoginMemberArgumentResolver;
import auth.interceptor.AdminInterceptor;
import auth.interceptor.LoginInterceptor;
import auth.jwt.JwtTokenProvider;
import auth.jwt.TokenExtractor;
import auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final UserAuthenticator userAuthenticator;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenExtractor tokenExtractor;
    private final LoginService loginService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtTokenProvider, userAuthenticator, tokenExtractor))
                .order(1)
                .addPathPatterns("/admin/**", "/reservations/**", "/reservation-waitings/**");
        registry.addInterceptor(new AdminInterceptor())
                .order(2)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService, tokenExtractor));
    }
}
