package nextstep;

import auth.AdminInterceptor;
import auth.JwtTokenProvider;
import auth.LoginMemberArgumentResolver;
import auth.LoginService;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    private final LoginService loginService;

    public WebMvcConfiguration(JwtTokenProvider jwtTokenProvider, LoginService loginService) {
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
