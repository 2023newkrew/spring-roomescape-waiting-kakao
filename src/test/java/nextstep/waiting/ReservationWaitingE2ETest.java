package nextstep.waiting;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

class ReservationWaitingE2ETest extends AbstractE2ETest {

    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private ReservationWaitingRequest reservationWaitingRequest;
    private ReservationRequest reservationRequest;
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

        reservationRequest = new ReservationRequest(
                scheduleId
        );
        reservationWaitingRequest = new ReservationWaitingRequest(
                scheduleId
        );
    }

    @DisplayName("예약이 없는 경우 예약 대기를 생성하면 바로 예약된다.")
    @Test
    void createWaitingWithoutReservation() {

        // given
        var response =  RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationWaitingRequest)

        // when
                .when().post("/reservation-waitings")
        // then
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value()).extract();
        Assertions.assertThat(response.header("Location").startsWith("/reservations/"))
                .isTrue();
    }


    @DisplayName("예약이 있는 경우 예약 대기를 생성한다.")
    @Test
    void createWaitingE2E() {
        createReservation();

        // given
        var response =  RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationWaitingRequest)

                // when
                .when().post("/reservation-waitings")
                // then
                .then().statusCode(HttpStatus.CREATED.value()).extract();
        Assertions.assertThat(response.header("Location").startsWith("/reservation-waitings/"))
                .isTrue();
    }

    @DisplayName("예약 대기를 삭제한다.")
    @Test
    void deleteWaiting() {
        createReservation();
        String id = createWaiting().header("Location").split("/")[2];
        RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)

            // when
                .when().delete("/reservation-waitings/" + id)
            // then
            .then().statusCode(HttpStatus.NO_CONTENT.value()).extract();
    }

    @DisplayName("자신의 예약 대기가 아닌 경우 삭제할 수 없다.")
    @Test
    void deleteWaitingFail() {
        createReservation();
        String id = createWaiting().header("Location").split("/")[2];
        RestAssured
                .given().log().all()
                .auth().oauth2(token2.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)

                // when
                .when().delete("/reservation-waitings/" + id)
                // then
                .then().statusCode(HttpStatus.FORBIDDEN.value()).extract();
    }

    @DisplayName("자신의 예약 대기 목록을 조회할 수 있다.")
    @Test
    void selectWaitingList() {
        // given
        createReservation();
        createWaiting();
        createWaiting();

        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())

        // when
                .when().get("/reservation-waitings/mine")

        // then
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<ReservationWaiting> reservationWaitings = response.jsonPath().getList(".", ReservationWaiting.class);
        Assertions.assertThat(reservationWaitings.size()).isEqualTo(2);
    }

    private ExtractableResponse<Response> createWaiting() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationWaitingRequest)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createReservation() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(reservationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }

    public String requestCreateSchedule() {
        ScheduleRequest body = new ScheduleRequest(1L, "2022-08-11", "13:00");
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .header("Location");
    }
}