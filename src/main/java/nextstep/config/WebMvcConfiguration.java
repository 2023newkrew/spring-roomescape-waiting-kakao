package nextstep.config;

import auth.domain.JwtTokenProvider;
import auth.presentation.LoginMemberArgumentResolver;
import auth.presentation.filter.AdminFilter;
import auth.presentation.filter.TokenFilter;
import auth.service.LoginService;
import nextstep.presentation.AuthExceptionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public LoginMemberArgumentResolver loginMemberArgumentResolver() {
        return new LoginMemberArgumentResolver(loginService);
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver());
    }

    @Bean
    public FilterRegistrationBean adminFilter() {
        FilterRegistrationBean adminFilterBean = new FilterRegistrationBean(new AdminFilter());

        adminFilterBean.setOrder(3);
        adminFilterBean.addUrlPatterns("/admin/*");

        return adminFilterBean;
    }

    @Bean
    public FilterRegistrationBean loginFilter() {
        FilterRegistrationBean loginFilterBean = new FilterRegistrationBean(new TokenFilter(jwtTokenProvider));

        loginFilterBean.setOrder(2);
        loginFilterBean.addUrlPatterns("/members/me", "/reservations/*", "/admin/*");

        return loginFilterBean;
    }

    @Bean
    public FilterRegistrationBean exceptionFilter() {
        FilterRegistrationBean loginFilterBean = new FilterRegistrationBean(new AuthExceptionFilter());

        loginFilterBean.setOrder(1);
        loginFilterBean.addUrlPatterns("/*");

        return loginFilterBean;
    }
}
