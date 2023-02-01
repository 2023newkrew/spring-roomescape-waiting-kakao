package nextstep;

import auth.AuthConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {AuthConfig.class})
public class AppConfig {
}
