package roomescape;

import auth.JWTAuthorization;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("web")
@Import(JWTAuthorization.class)
public class SpringWebApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringWebApplication.class)
                .profiles("web")
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}

