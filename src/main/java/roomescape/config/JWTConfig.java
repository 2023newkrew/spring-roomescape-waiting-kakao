package roomescape.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.controller.interceptor.AdminInterceptor;
import roomescape.controller.interceptor.JWTBearerInterceptor;
import roomescape.controller.resolver.JWTMemberIdResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JWTConfig implements WebMvcConfigurer {
    private final JWTBearerInterceptor jwtBearerInterceptor;
    private final AdminInterceptor adminInterceptor;
    private final JWTMemberIdResolver jwtMemberIdResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtBearerInterceptor)
                .order(1);
        registry.addInterceptor(adminInterceptor)
                .order(2);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtMemberIdResolver);
    }
}
