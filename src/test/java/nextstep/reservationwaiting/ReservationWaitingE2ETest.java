package nextstep.reservationwaiting;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservationwaiting.dto.ReservationWaitingRequest;
import nextstep.reservationwaiting.dto.ReservationWaitingResponse;
import nextstep.schedule.dto.ScheduleRequest;
import nextstep.theme.dto.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationWaitingE2ETest extends AbstractE2ETest {
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private ReservationRequest request;
    private ReservationWaitingRequest reservationWaitingRequest;
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

        request = new ReservationRequest(
                scheduleId
        );
        reservationWaitingRequest = new ReservationWaitingRequest(
                scheduleId
        );
    }

    @DisplayName("생성된 예약에 예약 대기를 신청한다")
    @Test
    void create() {
        createReservation();
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(reservationWaitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("존재하지 않는 예약에 예약 대기를 신청한다")
    @Test
    void createToNonExistReservation() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(reservationWaitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("예약 대기를 조회한다")
    @Test
    void show() {
        createReservation();
        createReservationWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<ReservationWaitingResponse> reservationWaitingResponses = response.jsonPath().getList(".", ReservationWaitingResponse.class);
        assertThat(reservationWaitingResponses.size()).isEqualTo(1);
    }

    @DisplayName("예약 대기를 조회한다(여러개)")
    @Test
    void showMany() {
        createReservation();
        createReservationWaiting();
        createReservationWaiting();
        createReservationWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<ReservationWaitingResponse> reservationWaitingResponses = response.jsonPath().getList(".", ReservationWaitingResponse.class);
        assertThat(reservationWaitingResponses.size()).isEqualTo(3);
    }

    @DisplayName("예약 대기가 없을 때 조회하면 빈 json이 반환된다.")
    @Test
    void showEmptyList() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<ReservationWaitingResponse> reservationWaitingResponses = response.jsonPath().getList(".", ReservationWaitingResponse.class);
        assertThat(reservationWaitingResponses.size()).isEqualTo(0);
    }

    @DisplayName("예약 대기를 삭제한다")
    @Test
    void delete() {
        createReservation();
        var reservationWaiting = createReservationWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(reservationWaiting.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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

    private ExtractableResponse<Response> createReservationWaiting() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(reservationWaitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
    }
}
