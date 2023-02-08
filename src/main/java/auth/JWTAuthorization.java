package auth;

import auth.controller.interceptor.AdminInterceptor;
import auth.controller.interceptor.JWTBearerInterceptor;
import auth.controller.interceptor.JWTExistInterceptor;
import auth.controller.resolver.JWTMemberIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "auth")
@RequiredArgsConstructor
public class JWTAuthorization implements WebMvcConfigurer {
    private final JWTBearerInterceptor jwtBearerInterceptor;
    private final AdminInterceptor adminInterceptor;
    private final JWTExistInterceptor jwtExistInterceptor;
    private final JWTMemberIdResolver jwtMemberIdResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtBearerInterceptor)
                .order(1);
        registry.addInterceptor(adminInterceptor)
                .order(2);
        registry.addInterceptor(jwtExistInterceptor)
                .order(2);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtMemberIdResolver);
    }
}
