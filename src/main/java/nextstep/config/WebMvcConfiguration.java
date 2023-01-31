package nextstep.config;

import auth.AdminInterceptor;
import auth.JwtTokenProvider;
import auth.LoginMemberArgumentResolver;
import auth.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final LoginService loginService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider))
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService));
    }
}
