package nextstep.waiting;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.reservation.Reservation;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WaitingE2ETest extends AbstractE2ETest {
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private WaitingRequest request;
    private Long themeId;
    private Long scheduleId;

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

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);
        var scheduleResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(scheduleRequest)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);

        request = new WaitingRequest(
                scheduleId
        );
    }

    @DisplayName("예약 대기를 생성한다")
    @Test
    void create() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).startsWith("/reservation-waitings");
    }

    @DisplayName("비로그인 사용자가 예약 대기를 생성한다")
    @Test
    void createWithoutLogin() {
        var response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약되지 않은 스케줄에 예약 대기하면 바로 예약된다")
    @Test
    void waitFreeSchedule() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).startsWith("/reservations");
    }

    @DisplayName("등록되지 않은 스케줄에 예약 대기하면 400 예외를 반환한다")
    @Test
    void waitInvalidSchedule() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(new WaitingRequest(-1L))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("내 예약 대기 목록을 조회한다")
    @Test
    void showMyWaitings() {
        createReservation();
        createWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<MyWaitingResponse> reservations = response.jsonPath().getList(".", MyWaitingResponse.class);
        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).getWaitNum()).isEqualTo(1);
    }

    @DisplayName("내 예약 대기 목록 조회를 토큰 없이 하면 401을 반환한다.")
    @Test
    void showMyWaitingsWithoutToken() {
        var response = RestAssured
                .given().log().all()
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


    private ExtractableResponse<Response> createReservation() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createWaiting() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
    }
}
