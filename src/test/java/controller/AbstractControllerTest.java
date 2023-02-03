package controller;

import auth.domain.UserDetails;
import auth.domain.UserRole;
import auth.dto.TokenRequest;
import auth.provider.JwtTokenProvider;
import auth.service.AuthenticationPrincipal;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.RoomEscapeApplication;
import nextstep.exception.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.hamcrest.Matchers.equalTo;


@SqlGroup(
        {
                @Sql("classpath:/dropTable.sql"),
                @Sql("classpath:/schema.sql"),
                @Sql("classpath:/testData.sql")
        })
@SpringBootTest(classes = RoomEscapeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractControllerTest {

    static String SECRET_KEY = "learning-test-spring";

    static int VALIDITY_IN_MILLISECONDS = 3_600_000;

    static AuthenticationPrincipal PRINCIPAL = username -> new UserDetails() {

        @Override
        public String getUsername() {
            return "admin";
        }

        @Override
        public String getPassword() {
            return "admin";
        }

        @Override
        public UserRole getRole() {
            return UserRole.ADMIN;
        }

        @Override
        public boolean isWrongPassword(String password) {
            return false;
        }

        @Override
        public boolean isNotAdmin() {
            return false;
        }
    };

    static JwtTokenProvider provider = new JwtTokenProvider(SECRET_KEY, VALIDITY_IN_MILLISECONDS, PRINCIPAL);

    String token;

    void setUpTemplate() {
        TokenRequest tokenRequest = new TokenRequest("admin", "admin");
        token = provider.createToken(tokenRequest).getAccessToken();
    }

    @BeforeEach
    final void setUp() {
        setUpTemplate();
    }

    <T> Response get(RequestSpecification given, String path, Object... pathParams) {
        return given
                .when()
                .get(path, pathParams);
    }

    <T> Response post(RequestSpecification given, String path, T request, Object... pathParams) {
        return given
                .body(request)
                .when()
                .post(path, pathParams);
    }

    <T> Response delete(RequestSpecification given, String path, Object... pathParams) {
        return given
                .when()
                .delete(path, pathParams);
    }

    RequestSpecification given() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    RequestSpecification authGiven() {
        return given()
                .auth().oauth2(token);
    }

    ValidatableResponse then(Response response) {
        return response.then().log().all();
    }

    void thenThrow(Response response, ErrorMessage expectedException) {
        then(response)
                .statusCode(expectedException.getHttpStatus().value())
                .body("message", equalTo(expectedException.getErrorMessage()));
    }
}
