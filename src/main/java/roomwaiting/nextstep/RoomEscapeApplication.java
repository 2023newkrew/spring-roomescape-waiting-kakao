package roomwaiting.nextstep;

import roomwaiting.auth.token.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import roomwaiting.support.ExceptionHandlers;


@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
@Import(ExceptionHandlers.class)
public class RoomEscapeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomEscapeApplication.class, args);
    }
}
