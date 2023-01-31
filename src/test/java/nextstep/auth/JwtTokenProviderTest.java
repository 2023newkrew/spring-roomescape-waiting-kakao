package nextstep.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import auth.exception.InvalidTokenException;
import auth.support.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtTokenProvider 학습 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Value("${security.jwt.token.secret-key}")
    private String DEFINED_SECRET_KEY;

    @Value("${security.jwt.token.expire-length}")
    private Long VALIDITY_IN_MILLISECONDS;

    @Test
    @DisplayName("만기되지 않은 토큰은 유효하다.")
    void test1() {
        String token = creteToken("1", DEFINED_SECRET_KEY, VALIDITY_IN_MILLISECONDS);

        assertThat(jwtTokenProvider.isValidToken(token)).isTrue();
    }

    @Test
    @DisplayName("만기되지 않은 토큰은 유효하다.")
    void test2() {
        String token = creteToken("1", DEFINED_SECRET_KEY, -1L);

        assertThat(jwtTokenProvider.isValidToken(token)).isFalse();
    }

    @Test
    @DisplayName("다른 비밀키로 생성된 token은 유효하지 않다.")
    void test3() {
        String tokenHeader = creteToken("1", "NOT_VALID_SECRET_KEY", VALIDITY_IN_MILLISECONDS);

        assertThat(jwtTokenProvider.isValidToken(tokenHeader)).isFalse() ;
    }

    @Test
    @DisplayName("발급된 토큰에서 subject를 추출할 수 있다.")
    void test4() {
        String subject = "1";
        String credential = jwtTokenProvider.createCredential(subject);
        String token = createToken(credential);

        assertThat(jwtTokenProvider.getSubject(token))
                .isEqualTo(subject);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("토큰은 Type과 Credential로 이루어져야 한다.")
    @ValueSource(strings = {"Bearer", "Bearer "})
    void test5(String token) {
        Assertions.assertThatThrownBy(() -> jwtTokenProvider.getCredential(token))
                .isInstanceOf(InvalidTokenException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Token"})
    @DisplayName("토큰 헤더에서 Credential을 추출할 수 있다.")
    void test6(String credential) {
        String token = createToken(credential);
        assertThat(jwtTokenProvider.getCredential(token))
                .isEqualTo(credential);
    }

    private String createToken(String credential) {
        return "Bearer " + credential;
    }

    private String creteToken(String subject, String secretKey, Long validityInMilliseconds) {
        return createToken(createCredential(secretKey, subject, validityInMilliseconds));
    }

    private String createCredential(String secretKey, String subject, Long validityInMilliseconds) {
        Claims claims = Jwts.claims().setSubject(subject);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}