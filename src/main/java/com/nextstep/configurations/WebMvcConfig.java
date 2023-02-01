package com.nextstep.configurations;

import com.authorizationserver.infrastructures.web.AdminInterceptor;
import com.authorizationserver.infrastructures.jwt.JwtTokenProvider;
import com.nextstep.infrastructures.web.UseContextArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public UseContextArgumentResolver memberIdArgumentResolver() {
        return new UseContextArgumentResolver(jwtTokenProvider());
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor(jwtTokenProvider());
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor())
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(memberIdArgumentResolver());
    }
}