package nextstep;

import io.restassured.RestAssured;
import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import nextstep.member.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractE2ETest {
    public static final String USERNAME1 = "username1";
    public static final String USERNAME2 = "username2";
    public static final String PASSWORD = "password";

    protected TokenResponse token;
    protected TokenResponse otherPersonToken;

    @BeforeEach
    protected void setUp() {
        MemberRequest memberBody1 = new MemberRequest(USERNAME1, PASSWORD, "name", "010-1234-5678", "ADMIN");
        MemberRequest memberBody2 = new MemberRequest(USERNAME2, PASSWORD, "name", "010-1234-5678", "USER");
        TokenRequest tokenBody1 = new TokenRequest(USERNAME1, PASSWORD);
        TokenRequest tokenBody2 = new TokenRequest(USERNAME2, PASSWORD);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody1)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        var response1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody1)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody2)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        var response2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody2)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        token = response1.as(TokenResponse.class);
        otherPersonToken = response2.as(TokenResponse.class);
    }
}
