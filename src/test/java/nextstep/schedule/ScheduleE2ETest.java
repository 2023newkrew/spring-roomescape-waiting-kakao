package nextstep.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import nextstep.AbstractE2ETest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ScheduleE2ETest extends AbstractE2ETest {

    private Long themeId;

    @BeforeEach
    public void setUp() {
        super.setUp();
        ThemeRequest themeRequest = new ThemeRequest(THEME_NAME, THEME_DESC, THEME_PRICE);
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(themeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] themeLocation = response.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);
    }

    @DisplayName("스케줄을 생성한다")
    @Test
    void create() {
        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);
        String scheduleLocation = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(scheduleRequest)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");
        assertTrue(scheduleLocation.matches("\\/schedules\\/[1-9][0-9]*"));
    }

    @DisplayName("일반 사용자는 스케줄을 생성할 수 없다")
    @Test
    void createByNotAdminUser() {
        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);
        RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(scheduleRequest)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("로그인하지 않은 경우 스케줄을 생성할 수 없다")
    @Test
    void createByNotLoggedInUser() {
        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(scheduleRequest)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("스케줄을 조회한다")
    @Test
    void showSchedules() {
        createSchedule();

        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", "2022-08-11")
                .when().get("/schedules")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getList(".", Schedule.class)).hasSize(1);
    }

    @DisplayName("스케줄을 삭제한다")
    @Test
    void delete() {
        Long id = createSchedule();

        RestAssured
                .given().log().all()
                .auth().oauth2(adminToken)
                .when().delete("/admin/schedules/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("일반 사용자는 스케줄을 삭제할 수 없다")
    @Test
    void deleteByNotAdminUser() {
        Long id = createSchedule();

        RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken)
                .when().delete("/admin/schedules/" + id)
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("로그인하지 않은 경우 스케줄을 삭제할 수 없다")
    @Test
    void deleteByNotLoggedInUser() {
        Long id = createSchedule();

        RestAssured
                .given().log().all()
                .when().delete("/admin/schedules/" + id)
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    Long createSchedule() {
        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);
        String location = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(scheduleRequest)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .header("Location");
        return Long.parseLong(location.split("/")[2]);
    }
}
