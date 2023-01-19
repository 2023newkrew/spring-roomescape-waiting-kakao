package roomescape.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.resolver.JWTBearerTokenSubjectResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ResolverConfig implements WebMvcConfigurer {
    private final JWTBearerTokenSubjectResolver jwtBearerTokenSubjectResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtBearerTokenSubjectResolver);
    }
}
