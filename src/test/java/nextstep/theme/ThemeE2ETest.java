package nextstep.theme;

import auth.login.TokenRequest;
import auth.login.TokenResponse;
import io.restassured.RestAssured;
import nextstep.AbstractE2ETest;
import nextstep.member.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

class ThemeE2ETest extends AbstractE2ETest {
    @DisplayName("테마를 생성한다")
    @Test
    void create() {
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);
        RestAssured
                .given()
                .log()
                .all()
                .auth()
                .oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post("/admin/themes")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("어드민이 아닌 사람이 테마를 생성한다")
    @Test
    void createFromNormalUser() {

        MemberRequest memberBody = new MemberRequest(USERNAME + 1, PASSWORD, "name", "010-1234-5678", "");
        RestAssured
                .given()
                .log()
                .all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when()
                .post("/members")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenBody = new TokenRequest(USERNAME + 1, PASSWORD);
        var response = RestAssured
                .given()
                .log()
                .all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when()
                .post("/login/token")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        token = response.as(TokenResponse.class);

        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);
        RestAssured
                .given()
                .log()
                .all()
                .auth()
                .oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post("/admin/themes")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("테마 목록을 조회한다")
    @Test
    void showThemes() {
        createTheme();

        var response = RestAssured
                .given()
                .log()
                .all()
                .param("date", "2022-08-11")
                .when()
                .get("/themes")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        assertThat(response.jsonPath()
                .getList(".")).hasSize(1);
    }

    @DisplayName("테마를 삭제한다")
    @Test
    void delete() {
        Long id = createTheme();

        var response = RestAssured
                .given()
                .log()
                .all()
                .auth()
                .oauth2(token.getAccessToken())
                .when()
                .delete("/admin/themes/" + id)
                .then()
                .log()
                .all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public Long createTheme() {
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);
        String location = RestAssured
                .given()
                .log()
                .all()
                .auth()
                .oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post("/admin/themes")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .header("Location");
        return Long.parseLong(location.split("/")[2]);
    }
}
