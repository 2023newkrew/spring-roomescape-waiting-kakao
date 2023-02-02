package nextstep;

import io.restassured.RestAssured;
import auth.TokenRequest;
import auth.TokenResponse;
import nextstep.member.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractE2ETest {
    public static final String USERNAME = "adminuser";
    public static final String PASSWORD = "password";
    public static final String SOMEONE_USERNAME = "someone";
    public static final String SOMEONE_PASSWORD = "pwdpwd";

    protected TokenResponse token;
    protected TokenResponse someoneToken;

    @BeforeEach
    protected void setUp() {
        MemberRequest myMmemberBody = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "ADMIN");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(myMmemberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenBody = new TokenRequest(USERNAME, PASSWORD);
        var myResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        token = myResponse.as(TokenResponse.class);

        MemberRequest someoneMemberBody = new MemberRequest(SOMEONE_USERNAME, SOMEONE_PASSWORD, "someone", "010-1234-5678", "NORMAL");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(someoneMemberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest someoneTokenBody = new TokenRequest(SOMEONE_USERNAME, SOMEONE_PASSWORD);
        var someoneResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(someoneTokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        someoneToken = someoneResponse.as(TokenResponse.class);

    }
}
