package nextstep.config;

import auth.service.LoginService;
import auth.support.JwtTokenProvider;
import auth.support.LoginMemberArgumentResolver;
import auth.support.interceptor.AdminInterceptor;
import auth.support.interceptor.CommonInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CommonInterceptor(jwtTokenProvider)).addPathPatterns("/**").excludePathPatterns("/swagger-ui/**").order(1);
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider)).addPathPatterns("/admin/**").excludePathPatterns("/swagger-ui/**").order(2);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService));
    }
}
