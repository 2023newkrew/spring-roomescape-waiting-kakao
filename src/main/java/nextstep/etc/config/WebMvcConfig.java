package nextstep.etc.config;

import auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import nextstep.etc.interceptor.AdminInterceptor;
import nextstep.etc.interceptor.JwtInterceptor;
import nextstep.etc.resolver.LoginUserArgumentResolver;
import nextstep.member.service.MemberPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @Value("${security.jwt.token.access-token-name}")
    private String accessTokenName;

    @Value("${security.jwt.token.login-user-name}")
    private String loginUserName;

    private final MemberPrincipal memberPrincipal;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, validityInMilliseconds, memberPrincipal);
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor(accessTokenName, loginUserName, jwtTokenProvider());
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor(loginUserName, jwtTokenProvider());
    }

    @Bean
    public LoginUserArgumentResolver loginUserArgumentResolver() {
        return new LoginUserArgumentResolver(loginUserName);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(jwtInterceptor()).order(1);
        registry
                .addInterceptor(adminInterceptor()).order(2)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver());
    }
}