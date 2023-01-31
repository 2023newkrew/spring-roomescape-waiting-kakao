package nextstep.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import auth.Role;
import auth.utils.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtTokenProviderTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("토큰을 정상적으로 생성한다.")
    void createToken() {
        String adminToken = jwtTokenProvider.createToken("1", Role.ADMIN);
        String userToken = jwtTokenProvider.createToken("1", Role.USER);
        assertThatNoException();
    }

    @Test
    @DisplayName("토큰의 principal을 정상적으로 가져온다.")
    void getPrincipal() {
        String token = jwtTokenProvider.createToken("1", Role.ADMIN);

        assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo("1");
    }

    @Test
    @DisplayName("토큰의 Role을 정상적으로 가져온다.")
    void getRole() {
        String token = jwtTokenProvider.createToken("1", Role.ADMIN);
        assertThat(jwtTokenProvider.getRole(token)).isEqualTo(Role.ADMIN);
    }
}
