package nextstep.auth;

import auth.domain.dto.TokenRequest;
import auth.domain.dto.TokenResponse;
import io.restassured.RestAssured;
import nextstep.DatabaseCleaner;
import nextstep.domain.dto.request.MemberRequest;
import nextstep.domain.dto.request.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import javax.xml.crypto.Data;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthE2ETest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @Autowired
    private DatabaseCleaner databaseCleaner;
    @BeforeEach
    void setUp() {
        databaseCleaner.execute();

        MemberRequest body = new MemberRequest("username", "password", "name", "010-1234-5678", "ADMIN");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("토큰을 생성한다")
    @Test
    public void Should_CreateTokenResponse_When_Login() {
        TokenRequest body = new TokenRequest(USERNAME, PASSWORD);
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.as(TokenResponse.class)).isNotNull();
    }
}
