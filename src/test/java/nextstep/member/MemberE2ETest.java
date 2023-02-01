package nextstep.member;

import io.restassured.RestAssured;
import auth.TokenRequest;
import auth.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static nextstep.auth.AuthE2ETest.createToken;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemberE2ETest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private TokenResponse token;

    @BeforeEach
    void setUp() {
        MemberRequest memberBody = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "ADMIN");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

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
    public void showThemes() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        Member member = response.as(Member.class);
        assertThat(member.getUsername()).isNotNull();
    }

    public static void createMember(MemberRequest memberRequest) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    public static TokenResponse createAdminMemberAndIssueToken() {
        String username = UUID.randomUUID().toString().substring(0, 10);
        String password = UUID.randomUUID().toString().substring(0, 10);
        MemberRequest memberRequest = new MemberRequest(username, password, "name", "010-1234-5678", "ADMIN");
        return createMemberAndIssueToken(memberRequest);
    }

    public static TokenResponse createNormalMemberAndIssueToken() {
        String username = UUID.randomUUID().toString().substring(0, 10);
        String password = UUID.randomUUID().toString().substring(0, 10);
        MemberRequest memberRequest = new MemberRequest(username, password, "name", "010-1234-5678", "MEMBER");
        return createMemberAndIssueToken(memberRequest);
    }

    private static TokenResponse createMemberAndIssueToken(MemberRequest memberRequest) {
        createMember(memberRequest);
        TokenRequest tokenRequest = new TokenRequest(memberRequest.getUsername(), memberRequest.getPassword());
        return createToken(tokenRequest);
    }
}
