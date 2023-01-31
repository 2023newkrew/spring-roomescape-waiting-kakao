package nextstep.waiting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.AbstractE2ETest;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class WaitingE2ETest extends AbstractE2ETest {
    private WaitingRequest waitingRequest;
    private Long themeId;
    private Long scheduleId;

    @BeforeEach
    public void setUp() {
        super.setUp();
        ThemeRequest themeRequest = new ThemeRequest(THEME_NAME, THEME_DESC, THEME_PRICE);
        var themeResponse = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(themeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);
        var scheduleResponse = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(scheduleRequest)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);

        ReservationRequest reservationRequest = new ReservationRequest(scheduleId);
        RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        waitingRequest = new WaitingRequest(scheduleId);
    }

    @DisplayName("예약 대기를 생성한다")
    @Test
    void create() {
        String waitingLocation = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken)
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");
        assertTrue(waitingLocation.matches("/reservations-waitings/[1-9][0-9]*"));
    }

    @DisplayName("비로그인 사용자가 예약 대기를 생성 시 실패한다")
    @Test
    void createWithoutLoginFail() {
        RestAssured
                .given().log().all()
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations-waitings")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약 대기를 삭제한다")
    @Test
    void delete() {
        var waiting = createWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken)
                .when().delete(waiting.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("없는 예약 대기를 삭제할 수 없다.")
    @Test
    void createNotExistWaiting() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken)
                .when().delete("/reservations-waitings/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("다른 사람의 예약 대기를 삭제할 수 없다.")
    @Test
    void deleteWaitingOfOtherUser() {
        var waiting = createWaiting();

        RestAssured
                .given().log().all()
                .auth().oauth2(adminToken)
                .when().delete(waiting.header("Location"))
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("내 예약 대기 목록을 조회한다")
    @Test
    void showMyWaitings() {
        createWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken)
                .when().get("/reservations-waitings/mine")
                .then().log().all()
                .extract();

        List<WaitingResponse> waitings = response.jsonPath().getList(".", WaitingResponse.class);
        assertThat(waitings).hasSize(1);
    }

    ExtractableResponse<Response> createWaiting() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(notAdminToken)
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations-waitings")
                .then().log().all()
                .extract();
    }
}
