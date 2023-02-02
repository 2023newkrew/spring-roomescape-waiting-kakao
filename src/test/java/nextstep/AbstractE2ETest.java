package nextstep;

import auth.TokenRequest;
import auth.TokenResponse;
import io.restassured.RestAssured;
import nextstep.member.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractE2ETest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    protected TokenResponse token;
    protected TokenResponse adminToken;

    @BeforeEach
    protected void setUp() {
        token = generateNewMemberToken(USERNAME, PASSWORD, "USER");
        adminToken = generateNewMemberToken("admin", "admin", "ADMIN");
    }

    protected TokenResponse generateNewMemberToken(String username, String password, String role) {
        MemberRequest memberBody = new MemberRequest(username, password, "name", "010-1234-5678", role);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        return getToken(username, password);
    }

    private static TokenResponse getToken(String username, String password) {
        TokenRequest tokenBody = new TokenRequest(username, password);
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        return response.as(TokenResponse.class);
    }
}
