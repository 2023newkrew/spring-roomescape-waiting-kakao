package nextstep;

import auth.AdminInterceptor;
import auth.JwtTokenProvider;
import auth.LoginMemberArgumentResolver;
import auth.LoginService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    public WebMvcConfiguration(LoginService loginService, JwtTokenProvider jwtTokenProvider) {
        this.loginService = loginService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider))
                .addPathPatterns("/admin/**")
                .addPathPatterns("/reservations/*/approve")
                .addPathPatterns("/reservations/*/cancel-approve");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService));
    }
}
