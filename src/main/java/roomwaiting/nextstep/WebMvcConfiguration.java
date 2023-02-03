package roomwaiting.nextstep;

import java.util.List;

import roomwaiting.auth.interceptor.LoginInterceptor;
import roomwaiting.auth.interceptor.AdminInterceptor;
import roomwaiting.auth.mvc.LoginController;
import roomwaiting.auth.principal.LoginMemberArgumentResolver;
import roomwaiting.auth.mvc.LoginService;
import roomwaiting.auth.token.JwtProperties;
import roomwaiting.auth.token.JwtTokenProvider;
import roomwaiting.auth.userdetail.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    public WebMvcConfiguration(UserDetailsService userDetailsService, JwtProperties jwtProperties, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtProperties = jwtProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(jwtProperties);
    }

    @Bean
    public LoginService loginService(JwtTokenProvider jwtTokenProvider) {
        return new LoginService(userDetailsService, jwtTokenProvider, passwordEncoder);
    }

    @Bean
    public LoginController loginController(LoginService loginService) {
        return new LoginController(loginService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/reservations/**")
                .addPathPatterns("/reservation-waitings/**")
                .addPathPatterns("/schedules/**")
                .addPathPatterns("/themes/**")
                .addPathPatterns("/admin/**");
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider()))
                .addPathPatterns("/admin/**")
                .addPathPatterns("/reservations/{id}/approve")
                .addPathPatterns("/reservations/{id}/cancel-approve");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService(jwtTokenProvider())));
    }
}
