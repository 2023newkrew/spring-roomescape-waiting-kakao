package auth;

import auth.dto.TokenRequest;
import auth.dto.TokenResponse;
import io.restassured.RestAssured;
import nextstep.RoomEscapeApplication;
import nextstep.member.MemberRequest;
import nextstep.role.Role;
import nextstep.theme.ThemeRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = RoomEscapeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthE2ETest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private Long memberId;

    @BeforeEach
    void setUp() {
        MemberRequest body = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", Role.ADMIN.name());
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("토큰을 생성한다")
    @Test
    public void create() {
        TokenRequest body = new TokenRequest(USERNAME, PASSWORD);
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        Assertions.assertThat(response.as(TokenResponse.class)).isNotNull();
    }

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

    public Long createTheme() {
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);
        String location = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");
        return Long.parseLong(location.split("/")[2]);
    }
}
