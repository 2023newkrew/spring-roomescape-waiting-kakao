package nextstep;

import auth.AuthConfig;
import auth.JwtTokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "auth",
        "nextstep"
})
public class RoomEscapeApplication {
    public static void main(String[] args) {
;        SpringApplication.run(RoomEscapeApplication.class, args);
    }
}
