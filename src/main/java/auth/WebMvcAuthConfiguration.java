package auth;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class WebMvcAuthConfiguration implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginService loginService;

    public WebMvcAuthConfiguration(JwtTokenProvider jwtTokenProvider, LoginService loginService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginService = loginService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider)).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService));
    }
}
