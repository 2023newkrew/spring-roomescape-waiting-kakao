package nextstep.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import nextstep.AbstractE2ETest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ThemeE2ETest extends AbstractE2ETest {
    @DisplayName("테마를 생성한다")
    @Test
    void create() {
        ThemeRequest body = new ThemeRequest(THEME_NAME, THEME_DESC, THEME_PRICE);
        String themeLocation = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");
        assertTrue(themeLocation.matches("\\/themes\\/[1-9][0-9]*"));
    }

    @DisplayName("일반 사용자는 테마를 생성할 수 없다.")
    @Test
    void createByNotAdminUser() {
        ThemeRequest themeRequest = new ThemeRequest(THEME_NAME, THEME_DESC, THEME_PRICE);
        RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(themeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("로그인하지 않은 경우 테마를 생성할 수 없다.")
    @Test
    void createByNotLoggedInUser() {
        ThemeRequest themeRequest = new ThemeRequest(THEME_NAME, THEME_DESC, THEME_PRICE);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(themeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("빈 테마 목록을 조회한다")
    @Test
    void showEmpty() {
        var response = RestAssured
                .given().log().all()
                .param(DATE, TIME)
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        assertThat(response.jsonPath().getList(".", Theme.class)).isEmpty();
    }

    @DisplayName("테마 목록을 조회한다")
    @Test
    void showAll() {
        createTheme();

        var response = RestAssured
                .given().log().all()
                .param(DATE, TIME)
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        assertThat(response.jsonPath().getList(".", Theme.class)).hasSize(1);
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void delete() {
        Long id = createTheme();

        RestAssured
                .given().log().all()
                .auth().oauth2(adminToken)
                .when().delete("/admin/themes/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("일반 사용자는 테마를 삭제할 수 없다.")
    @Test
    void deleteByNotAdminUser() {
        Long id = createTheme();

        RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken)
                .when().delete("/admin/themes/" + id)
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("로그인하지 않은 경우 테마를 삭제할 수 없다.")
    @Test
    void deleteByNotLoggedInUser() {
        Long id = createTheme();

        RestAssured
                .given().log().all()
                .when().delete("/admin/themes/" + id)
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    Long createTheme() {
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);
        String location = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");
        return Long.parseLong(location.split("/")[2]);
    }
}
