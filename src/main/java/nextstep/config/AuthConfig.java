package nextstep.config;

import auth.config.AuthConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(AuthConfiguration.class)
@Configuration
public class AuthConfig {
}
