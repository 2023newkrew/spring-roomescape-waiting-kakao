package nextstep;

import auth.AuthConfig;
import auth.interceptor.AdminInterceptor;
import auth.service.LoginService;
import auth.support.LoginMemberArgumentResolver;
import auth.token.JwtTokenExtractor;
import auth.token.JwtTokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@Import(AuthConfig.class)
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;

    public WebMvcConfiguration(LoginService loginService, JwtTokenProvider jwtTokenProvider, JwtTokenExtractor jwtTokenExtractor) {
        this.loginService = loginService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenExtractor = jwtTokenExtractor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider, jwtTokenExtractor)).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService, jwtTokenExtractor));
    }
}
