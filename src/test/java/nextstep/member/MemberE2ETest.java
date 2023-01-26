package nextstep.member;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.core.Is.is;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemberE2ETest {
    @DisplayName("멤버를 생성한다")
    @Test
    public void create() {
        MemberRequest body = new MemberRequest("username", "password", "name", "010-1234-5678");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("토큰을 통해 내 정보를 조회한다.")
    @Test
    void me() {
        // given
        final String username = "davi-kane";
        final String password = "password";
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MemberRequest(username, password, "name", "010-1234-5678"))
                .post("/members");

        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(username, password))
                .post("/login/token");

        TokenResponse tokenResponse = response.as(TokenResponse.class);

        // when
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", "Bearer " + tokenResponse.getAccessToken()))
                .when().get("/members/me")

        // then
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("username", is(username))
                .body("password", is(password));
    }

    @DisplayName("토큰정보가 없으면 에러가 발생한다.")
    @Test
    void noToken() {
        RestAssured
                // given
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(new Header("Authorization", ""))

                // when
                .when().get("/members/me")

                // then
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    //
//    @DisplayName("테마 목록을 조회한다")
//    @Test
//    public void showThemes() {
//        createTheme();
//
//        var response = RestAssured
//                .given().log().all()
//                .param("date", "2022-08-11")
//                .when().get("/themes")
//                .then().log().all()
//                .statusCode(HttpStatus.OK.value())
//                .extract();
//        assertThat(response.jsonPath().getList(".").size()).isEqualTo(1);
//    }
//
//    @DisplayName("테마를 삭제한다")
//    @Test
//    void delete() {
//        Long id = createTheme();
//
//        var response = RestAssured
//                .given().log().all()
//                .when().delete("/themes/" + id)
//                .then().log().all()
//                .extract();
//
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//    }
//
//    public Long createTheme() {
//        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);
//        String location = RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(body)
//                .when().post("/themes")
//                .then().log().all()
//                .statusCode(HttpStatus.CREATED.value())
//                .extract().header("Location");
//        return Long.parseLong(location.split("/")[2]);
//    }
}
