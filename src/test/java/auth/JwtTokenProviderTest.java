package auth;

import lombok.RequiredArgsConstructor;
import nextstep.RoomEscapeApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtTokenProvider 학습 테스트")
@SpringBootTest(classes = RoomEscapeApplication.class)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void createToken() {
        String token = jwtTokenProvider.createToken("1", "ADMIN");

        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    void getPrincipal() {
        String token = jwtTokenProvider.createToken("1", "ADMIN");

        assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo("1");
    }

    @Test
    void getRole() {
        String token = jwtTokenProvider.createToken("1", "ADMIN");

        assertThat(jwtTokenProvider.getRole(token)).isEqualTo("ADMIN");
    }
}