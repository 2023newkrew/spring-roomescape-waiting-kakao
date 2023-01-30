package nextstep.auth;

import auth.support.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtTokenProvider 학습 테스트")
@SpringBootTest
class JwtTokenProviderTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void Should_ReturnTrue_When_ValidateToken() {
        String token = jwtTokenProvider.createToken("1", "ADMIN");

        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    void Should_ReturnId_When_GetPrincipal() {
        String token = jwtTokenProvider.createToken("1", "ADMIN");

        assertThat(jwtTokenProvider.getPrincipal(token)).isEqualTo("1");
    }

    @Test
    void Should_ReturnAdmin_When_GetRoleOfAdminToken() {
        String token = jwtTokenProvider.createToken("1", "ADMIN");

        assertThat(jwtTokenProvider.getRole(token)).isEqualTo("ADMIN");
    }
}