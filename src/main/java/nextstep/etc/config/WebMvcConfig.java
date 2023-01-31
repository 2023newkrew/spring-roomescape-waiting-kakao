package nextstep.etc.config;

import nextstep.auth.interceptor.AdminInterceptor;
import nextstep.auth.resolver.MemberIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberIdArgumentResolver memberIdArgumentResolver;

    private final AdminInterceptor adminInterceptor;

    public WebMvcConfig(MemberIdArgumentResolver memberIdArgumentResolver, AdminInterceptor adminInterceptor) {
        this.memberIdArgumentResolver = memberIdArgumentResolver;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

        argumentResolvers.add(memberIdArgumentResolver);
    }
}