package nextstep;

import auth.AuthConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({AuthConfiguration.class})
@SpringBootApplication
public class RoomEscapeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomEscapeApplication.class, args);
    }
}
