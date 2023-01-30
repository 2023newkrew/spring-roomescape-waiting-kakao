package nextstep.theme;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import nextstep.theme.model.Theme;
import nextstep.theme.model.ThemeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

public class ThemeTestUtil {
    public static final ThemeRequest DEFAULT_THEME_REQUEST = new ThemeRequest("테마이름", "테마설명", 22000);

    public static List<Theme> getThemes(String date) {
        return requestThemesAndGetValidatableResponse(date)
                .extract()
                .jsonPath()
                .getList(".", Theme.class);
    }

    public static ValidatableResponse requestThemesAndGetValidatableResponse(String date) {
        return RestAssured
                .given().log().all()
                .param("date", date)
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    public static ValidatableResponse deleteThemeAndGetValidatableResponse(Long id, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/admin/themes/" + id)
                .then().log().all();
    }

    public static ValidatableResponse createThemeAndGetValidatableResponse(ThemeRequest themeRequest, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(themeRequest)
                .when().post("/admin/themes")
                .then().log().all();
    }
}