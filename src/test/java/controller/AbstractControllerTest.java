package controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import nextstep.RoomEscapeApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;


@SqlGroup(
        {
                @Sql("classpath:/dropTable.sql"),
                @Sql("classpath:/schema.sql")
        })
@SpringBootTest(classes = RoomEscapeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractControllerTest {

    <T> Response get(String path, Object... pathParams) {
        return given()
                .when()
                .get(path, pathParams);
    }

    <T> Response post(String path, T request, Object... pathParams) {
        return given()
                .body(request)
                .when()
                .post(path, pathParams);
    }

    <T> Response delete(String path, Object... pathParams) {
        return given()
                .when()
                .delete(path, pathParams);
    }

    RequestSpecification given() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    ValidatableResponse then(Response response) {
        return response.then().log().all();
    }
}
