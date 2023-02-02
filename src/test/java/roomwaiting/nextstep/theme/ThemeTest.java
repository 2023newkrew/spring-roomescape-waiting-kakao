package roomwaiting.nextstep.theme;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomwaiting.nextstep.RoomEscapeApplication;
import roomwaiting.AcceptanceTestExecutionListener;
import roomwaiting.nextstep.schedule.Schedule;
import roomwaiting.nextstep.schedule.ScheduleDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import roomwaiting.nextstep.AbstractE2ETest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static roomwaiting.support.Messages.ID;
import static roomwaiting.support.Messages.THEME_NOT_FOUND;

@SpringBootTest(classes = {RoomEscapeApplication.class})
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ThemeTest extends AbstractE2ETest{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ThemeDao themeDao;
    private ScheduleDao scheduleDao;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("허용되지 않은 사용자가 테마를 이용할 때, 에러가 발생한다")
    @Test
    void notAuthorizedUserTest(){
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("테마를 생성할 수 있다")
    @Test
    void create() {
        ExtractableResponse<Response> createdTheme = requestCreateTheme();
        assertThat(createdTheme.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("중복 테마를 생성할 경우, 에러가 발생한다")
    @Test
    void duplicateCreateTest(){
        requestCreateTheme();
        ExtractableResponse<Response> createdTheme = requestCreateTheme();
        assertThat(createdTheme.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("테마를 조회할 수 있다")
    @Test
    public void showThemes() {
        requestCreateTheme();
        var response = RestAssured
                .given().log().all()
                .param("date", "2022-08-11")
                .auth().oauth2(token.getAccessToken())
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(1);
    }

    @DisplayName("스케줄이 없는 경우, 테마를 삭제할 수 있다")
    @Test
    void delete() {
        ExtractableResponse<Response> createdTheme = requestCreateTheme();
        Long id = getThemeId(createdTheme);
        RestAssured
            .given().log().all()
                .auth().oauth2(token.getAccessToken())
            .when().delete("/admin/themes/" + id)
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("스케줄이 있는데 테마를 삭제하는 경우, 에러 발생")
    @Test
    void deleteErrorTest(){
        themeDao = new ThemeDao(jdbcTemplate);
        scheduleDao = new ScheduleDao(jdbcTemplate);
        ExtractableResponse<Response> createdTheme = requestCreateTheme();
        Long id = getThemeId(createdTheme);
        Theme theme = themeDao.findById(id).orElseThrow(() ->
                new NullPointerException(THEME_NOT_FOUND.getMessage() + ID + id));
        scheduleDao.save(new Schedule(
                theme, LocalDate.parse("2022-01-01"), LocalTime.parse("12:00:00")
        ));

        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/admin/themes/" + id)
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @DisplayName("없는 테마를 삭제할 경우, 에러가 발생한다")
    @Test
    void emptyDeleteTest(){
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/admin/themes/" + 1212L)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> requestCreateTheme() {
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .extract();
    }

    private Long getThemeId(ExtractableResponse<Response> requestTheme){
        String location = requestTheme.header("Location");
        return Long.parseLong(location.split("/")[2]);
    }
}
