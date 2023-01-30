package nextstep.waiting;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitingE2ETest extends AbstractE2ETest {
    private Long themeId;
    private Long scheduleId0;

    @BeforeEach
    public void setUp() {
        super.setUp();
        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
        var themeResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(themeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);


        var scheduleResponse0 = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ScheduleRequest(themeId, "2022-08-11", "13:00"))
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] scheduleLocation0 = scheduleResponse0.header("Location").split("/");
        scheduleId0 = Long.parseLong(scheduleLocation0[scheduleLocation0.length - 1]);
    }



    @DisplayName("예약을 순차적으로 시도해 예약 대기를 유도한다.")
    @Test
    void createNotExist() {
        var responseReservation = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(new WaitingRequest(scheduleId0))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(responseReservation.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(responseReservation.header("Location")).startsWith("/reservations/");

        var responseWaiting = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(new WaitingRequest(scheduleId0))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(responseWaiting.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(responseWaiting.header("Location")).startsWith("/reservation-waitings/");
    }

    @DisplayName("예약 대기를 삭제한다")
    @Test
    void delete() {
        var waiting = createWaiting(1);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(waiting.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("다른 사람이 예약 대기를 삭제한다")
    @Test
    void deleteWaitingOfOthers() {
        createWaiting(1);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2("other-token")
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("나의 예약 대기 목록을 조회한다.")
    @Test
    void showMyWaiting() {
        createWaiting(2);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        var myWaiting = response.jsonPath().getList(".", MyWaiting.class);
        assertThat(myWaiting)
                .hasSize(2)
                .satisfies((elem)->{
                    assertThat(elem.get(0).getWaitNum()).isEqualTo(1);
                    assertThat(elem.get(1).getWaitNum()).isEqualTo(2);
                })
        ;


    }

    private ExtractableResponse<Response> createWaiting(int scheduleCount) {
        var scheduleResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ScheduleRequest(themeId, "2022-08-11", "13:00"))
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        var scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);

        for (int i = 0; i < scheduleCount; i++) {
            RestAssured
                    .given().log().all()
                    .auth().oauth2(token.getAccessToken())
                    .body(new WaitingRequest(scheduleId))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/reservation-waitings")
                    .then().log().all();
        }

        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(new WaitingRequest(scheduleId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
    }

}
