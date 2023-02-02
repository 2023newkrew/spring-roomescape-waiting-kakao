package com.nextstep.configurations;

import com.authorizationserver.configurations.AuthMvcConfiguration;
import com.authorizationserver.domains.authorization.AuthRepository;
import com.authorizationserver.domains.authorization.AuthService;
import com.authorizationserver.infrastructures.jwt.JwtTokenProvider;
import com.authorizationserver.infrastructures.web.AdminInterceptor;
import com.authorizationserver.interfaces.AuthController;
import com.nextstep.domains.member.MemberDao;
import com.nextstep.infrastructures.web.UserContextArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@Import(AuthMvcConfiguration.class)
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final JdbcTemplate jdbcTemplate;

    public WebMvcConfiguration(JwtTokenProvider jwtTokenProvider, JdbcTemplate jdbcTemplate) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider)).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new UserContextArgumentResolver(authService()));
    }

    @Bean
    AuthService authService() {
        return new AuthService(authRepository(), jwtTokenProvider);
    }

    @Bean
    AuthController authController() {
        return new AuthController(authService());
    }

    private AuthRepository authRepository() {
        return new MemberDao(jdbcTemplate);
    }
}
