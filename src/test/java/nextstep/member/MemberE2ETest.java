package nextstep.member;

import static org.assertj.core.api.Assertions.assertThat;

import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import io.restassured.RestAssured;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemberE2ETest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private TokenResponse token;
    private Long memberId;

    @BeforeEach
    void setUp() {
        MemberRequest memberBody = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "ADMIN");
        memberId = Long.parseLong(RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value()).extract().header("Location").split("/")[2]);

        TokenRequest tokenBody = new TokenRequest(USERNAME, PASSWORD);
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        token = response.as(TokenResponse.class);
    }

    @DisplayName("멤버를 생성한다")
    @Test
    public void create() {
        MemberRequest body = new MemberRequest("username", "password", "name", "010-1234-5678", "ADMIN");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("내 정보를 조회한다")
    @Test
    public void showMyMemberInfo() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/members/" + memberId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        MemberResponse member = response.as(MemberResponse.class);
        assertThat(member.getUsername()).isNotNull();
    }

    @DisplayName("다른 사람의 정보를 조회하면 에러가 발생한다.")
    @Test
    void showDifferentMemberInfo() {
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/members/" + (memberId + 1L))
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract();
    }

}
