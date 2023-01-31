package nextstep;

import auth.AdminInterceptor;
import auth.JwtTokenProvider;
import auth.LoginService;
import auth.LoginUserDetailsArgumentResolver;
import lombok.RequiredArgsConstructor;
import nextstep.config.annotation.LoginMemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider)).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginUserDetailsArgumentResolver(loginService));
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService));
    }
}
