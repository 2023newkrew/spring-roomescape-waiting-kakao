package nextstep.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private final String VERSION = "v0.1";
    private final String API_TITLE = "스프링 예약 관리 미션 API";
    private final String API_DESCRIPTION = "카카오 신입사원 교육 - 2023 API";
    private final String AUTH_TYPE = "JWT 인증";
    private final String TOKEN_TYPE = "bearer";
    private final String JWT = "JWT";
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version(VERSION)
                .title(API_TITLE)
                .description(API_DESCRIPTION);

        String jwtSchemeName = AUTH_TYPE;
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(TOKEN_TYPE)
                        .bearerFormat(JWT));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
