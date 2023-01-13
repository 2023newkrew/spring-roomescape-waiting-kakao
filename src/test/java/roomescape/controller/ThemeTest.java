package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import roomescape.SpringWebApplication;
import roomescape.dto.ThemeControllerPostBody;
import roomescape.entity.Theme;

import java.util.UUID;
import java.util.random.RandomGenerator;

import static org.hamcrest.Matchers.is;

@DisplayName("테마에 관련된 RESTful API 요청")
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"web"})
public class ThemeTest {
    @Value("${local.server.port}")
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("테마 생성")
    @Test
    public void createTheme() {
        RestAssured
                .given()
                .contentType("application/json")
                .body(new ThemeControllerPostBody(
                        UUID.randomUUID().toString().split("-")[0],
                        UUID.randomUUID().toString(),
                        RandomGenerator.getDefault().nextInt(0, 1000000)
                ))
                .when()
                .post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("테마 검색")
    @Test
    public void getTheme() {
        var expectedResult = new ThemeControllerPostBody(
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString(),
                RandomGenerator.getDefault().nextInt(0, 1000000)
        );
        var postResult = RestAssured
                .given()
                .contentType("application/json")
                .body(expectedResult)
                .post("/themes");
        postResult.jsonPath().getList("exlist", Theme.class);
        RestAssured
                .when()
                .get(postResult.header("Location"))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(postResult.jsonPath().getInt("id")))
                .body("name", is(expectedResult.getName()))
                .body("desc", is(expectedResult.getDesc()))
                .body("price", is(expectedResult.getPrice()));
    }

    @DisplayName("테마 삭제")
    @Test
    public void getThemes() {
        var expectedResult = new ThemeControllerPostBody(
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString(),
                RandomGenerator.getDefault().nextInt(0, 1000000)
        );
        var postResult = RestAssured
                .given()
                .contentType("application/json")
                .body(expectedResult)
                .post("/themes");

        RestAssured
                .when()
                .get(postResult.header("Location"))
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
        RestAssured.delete(postResult.header("Location"));
        RestAssured
                .when()
                .get(postResult.header("Location"))
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
