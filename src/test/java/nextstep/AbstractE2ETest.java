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

    public static final String ADMIN_USERNAME = "adminUsername";
    public static final String ADMIN_PASSWORD = "adminPassword";
    public static final String ADMIN_NAME = "adminName";
    public static final String ADMIN_PHONE = "010-1234-5679";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String PHONE = "010-1234-5678";
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";
    public static final String THEME_NAME = "테마이름";
    public static final String THEME_DESC = "테마설명";
    public static final int THEME_PRICE = 22_000;
    protected String adminToken;
    protected String notAdminToken;

    @BeforeEach
    public void setUp() {
        TokenRequest adminTokenRequest = new TokenRequest(ADMIN_USERNAME, ADMIN_PASSWORD);
        adminToken = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(adminTokenRequest)
                .when().post("/login/token")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().as(TokenResponse.class)
                .getAccessToken();

        MemberRequest memberRequest = new MemberRequest(USERNAME, PASSWORD, NAME, PHONE);
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenRequest = new TokenRequest(USERNAME, PASSWORD);

        notAdminToken = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(TokenResponse.class)
                .getAccessToken();
    }
}
