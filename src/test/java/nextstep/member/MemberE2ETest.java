package nextstep.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import auth.TokenRequest;
import auth.TokenResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberE2ETest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String PHONE = "010-1234-5678";
    private String accessToken;

    @DisplayName("멤버를 생성한다")
    @Test
    void create() {
        MemberRequest memberRequest = new MemberRequest(USERNAME, PASSWORD, NAME, PHONE);
        String memberLocation = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");
        assertTrue(memberLocation.matches("\\/members\\/[1-9][0-9]*"));
    }

    @DisplayName("내 정보를 조회한다")
    @Test
    void showMyInformation() {
        createMember();

        TokenRequest tokenBody = new TokenRequest(USERNAME, PASSWORD);
        TokenResponse response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(TokenResponse.class);

        accessToken = response.getAccessToken();

        MemberResponse memberResponse = RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(MemberResponse.class);

        assertThat(memberResponse.getUsername()).isEqualTo("username");
        assertThat(memberResponse.getPassword()).isEqualTo("password");
        assertThat(memberResponse.getName()).isEqualTo("name");
        assertThat(memberResponse.getPhone()).isEqualTo("010-1234-5678");
    }

    @DisplayName("토큰 없이 멤버 정보를 조회할 수 없다")
    @Test
    void showMyInformationWithoutToken() {
        createMember();

        RestAssured
                .given().log().all()
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    void createMember() {
        MemberRequest memberBody = new MemberRequest(USERNAME, PASSWORD, NAME, PHONE);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
