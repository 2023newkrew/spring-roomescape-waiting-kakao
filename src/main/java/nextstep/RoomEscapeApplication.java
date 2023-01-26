package nextstep;

import auth.AuthConfig;
import auth.JwtTokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {
        "auth",
        "nextstep"
})
public class RoomEscapeApplication {
    public static void main(String[] args) {
;        SpringApplication.run(RoomEscapeApplication.class, args);
    }
}
