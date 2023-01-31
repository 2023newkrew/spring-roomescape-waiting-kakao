package nextstep;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(auth.AuthConfiguration.class)
public class AuthConfiguration {
}
