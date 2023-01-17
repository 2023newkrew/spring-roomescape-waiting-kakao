package roomescape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.resolver.JWTBearerTokenSubjectResolver;

import java.util.List;

@Configuration
public class ResolverConfig implements WebMvcConfigurer {

    private final JWTBearerTokenSubjectResolver jwtBearerTokenSubjectResolver;

    public ResolverConfig(JWTBearerTokenSubjectResolver jwtBearerTokenSubjectResolver) {
        this.jwtBearerTokenSubjectResolver = jwtBearerTokenSubjectResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtBearerTokenSubjectResolver);
    }
}
