package controller;

import com.authorizationserver.infrastructures.jwt.TokenData;
import com.authorizationserver.infrastructures.jwt.JwtTokenProvider;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import com.nextstep.RoomEscapeApplication;
import com.nextstep.interfaces.exceptions.ErrorMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.hamcrest.Matchers.equalTo;


@SqlGroup(
        {
                @Sql("classpath:/dropTable.sql"),
                @Sql("classpath:/schema.sql")
        })
@SpringBootTest(classes = RoomEscapeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractControllerTest {

    @Autowired
    JwtTokenProvider provider;

    String token;

    @BeforeEach
    void init() {
        token = provider.createToken(new TokenData(1L, "ADMIN"));
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

    <T> Response patch(RequestSpecification given, String path, Object... pathParams) {
        return given
                .when()
                .patch(path, pathParams);
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

    void thenThrow(Response response, ErrorMessageType expectedException) {
        then(response)
                .statusCode(expectedException.getHttpStatus().value())
                .body("message", equalTo(expectedException.getErrorMessage()));
    }
}
