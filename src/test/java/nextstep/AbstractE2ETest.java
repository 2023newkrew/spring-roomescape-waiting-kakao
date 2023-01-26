package nextstep;

import auth.dto.request.TokenRequest;
import auth.dto.response.TokenResponse;
import io.restassured.RestAssured;
import nextstep.dto.request.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractE2ETest {
    public static final String ADMIN_USERNAME = "admin_username";
    public static final String ADMIN_PASSWORD = "admin_password";

    public static final String USER_USERNAME = "user_username";
    public static final String USER_PASSWORD = "user_password";

    protected TokenResponse adminToken;
    protected TokenResponse userToken;

    @BeforeEach
    protected void setUp() {
        MemberRequest memberBody = new MemberRequest(ADMIN_USERNAME, ADMIN_PASSWORD, "admin", "010-1234-5678", "ADMIN");
        TokenRequest tokenBody = new TokenRequest(ADMIN_USERNAME, ADMIN_PASSWORD);
        adminToken = createMember(memberBody, tokenBody);

        MemberRequest userBody = new MemberRequest(USER_USERNAME, USER_PASSWORD, "user", "010-1234-5678", "USER");
        TokenRequest userTokenBody = new TokenRequest(USER_USERNAME, USER_PASSWORD);
        userToken = createMember(userBody, userTokenBody);
    }

    private TokenResponse createMember(MemberRequest memberRequest, TokenRequest tokenRequest) {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        return response.as(TokenResponse.class);
    }
}
