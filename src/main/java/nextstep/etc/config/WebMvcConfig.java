package nextstep.etc.config;

import auth.interceptor.AdminInterceptor;
import auth.provider.JwtTokenProvider;
import auth.resolver.MemberIdArgumentResolver;
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
    public MemberIdArgumentResolver memberIdArgumentResolver() {
        return new MemberIdArgumentResolver(jwtTokenProvider());
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