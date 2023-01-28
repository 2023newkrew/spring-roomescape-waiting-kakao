package nextstep.config;

import auth.presentation.interceptor.AdminInterceptor;
import auth.support.JwtTokenProvider;
import auth.presentation.argumentresolver.AuthenticationPrincipalArgumentResolver;
import auth.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private LoginService loginService;
    private JwtTokenProvider jwtTokenProvider;

    public WebMvcConfiguration(LoginService loginService, JwtTokenProvider jwtTokenProvider) {
        this.loginService = loginService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor(jwtTokenProvider);
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver loginMemberArgumentResolver(){
        return new AuthenticationPrincipalArgumentResolver(loginService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor()).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver());
    }
}
