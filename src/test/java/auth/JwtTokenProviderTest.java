package auth;

import auth.token.JwtTokenProvider;
import nextstep.RoomEscapeApplication;
import nextstep.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtTokenProvider 학습 테스트")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = RoomEscapeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JwtTokenProviderTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void createToken() {
        String token = jwtTokenProvider.createToken("1", "kakao", Role.ADMIN.name());

        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    void getPrincipal() {
        String token = jwtTokenProvider.createToken("1", "kakao", Role.ADMIN.name());

        assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo("1");
    }

    @Test
    void getRole() {
        String token = jwtTokenProvider.createToken("1", "kakao", Role.ADMIN.name());

        assertThat(jwtTokenProvider.getRole(token)).isEqualTo(Role.ADMIN.name());
    }

    @Test
    void getUsername() {
        String token = jwtTokenProvider.createToken("1", "kakao", Role.ADMIN.name());

        assertThat(jwtTokenProvider.getUsername(token)).isEqualTo("kakao");
    }
}