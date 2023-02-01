package nextstep;

import auth.TokenRequest;
import auth.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.member.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractE2ETest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    protected TokenResponse token;

    @BeforeEach
    protected void setUp() {
        MemberRequest memberBody = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "ADMIN");
        then(post(given(), "/members", memberBody))
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenBody = new TokenRequest(USERNAME, PASSWORD);
        var response = then(post(given(), "/login/token", tokenBody))
                .statusCode(HttpStatus.OK.value())
                .extract();

        token = response.as(TokenResponse.class);
    }

    protected RequestSpecification given() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    protected RequestSpecification givenWithAuth() {
        return given()
                .auth().oauth2(token.getAccessToken());
    }

    protected <T> Response post(RequestSpecification given, String path, T request, Object... pathParams) {
        return given
                .body(request)
                .when().post(path, pathParams);
    }

    protected <T> Response get(RequestSpecification given, String path, Object... pathParams) {
        return given
                .when().get(path, pathParams);
    }

    protected <T> Response get(RequestSpecification given, String path, Map<String, ?> pathParams) {
        return given
                .when().get(path, pathParams);
    }

    protected <T> Response delete(RequestSpecification given, String path, Object... pathParams) {
        return given
                .when().delete(path, pathParams);
    }

    protected ValidatableResponse then(Response response) {
        return response.then().log().all();
    }
}
