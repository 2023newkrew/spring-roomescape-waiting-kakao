package nextstep.reservation_waiting;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationWaitingE2ETest extends AbstractE2ETest {
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private Long reservationId;
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
        themeId = getIdFromResponse(themeResponse);

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
        scheduleId = getIdFromResponse(scheduleResponse);

        ReservationRequest reservationRequest = new ReservationRequest(scheduleId);
        var reservationResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(reservationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
        reservationId = getIdFromResponse(reservationResponse);
    }

    private Long getIdFromResponse(ExtractableResponse response) {
        String[] locations = response.header("Location").split("/");
        String id = locations[locations.length - 1];
        return Long.parseLong(id);
    }

    @DisplayName("예약 대기를 신청한다")
    @Test
    void create() {
        ReservationRequest reservationRequest = new ReservationRequest(scheduleId);
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(reservationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다")
    @Test
    void waitScheduleWithNoReservation() {
        deleteReservation();
        createReservationWaiting();


        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(1);
    }

    private ExtractableResponse<Response> createReservationWaiting() {
        ReservationRequest reservationRequest = new ReservationRequest(scheduleId);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(reservationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private ExtractableResponse<Response> deleteReservation() {
        return RestAssured
                .given().auth().oauth2(token.getAccessToken()).log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    @DisplayName("예약 대기 취소를 할 수 있다")
    @Test
    void reservationWaitingDeleteTest() {
        var response = createReservationWaiting();
        Long reservationWaitingId = getIdFromResponse(response);

        RestAssured
                .given().auth().oauth2(token.getAccessToken()).log().all()
                .when().delete("/reservation-waitings/" + reservationWaitingId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();

    }

    @Test
    void getMyReservationWaitingListTest() {
        createReservationWaiting();

        var response = RestAssured
                .given().auth().oauth2(token.getAccessToken()).log().all()
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<ReservationWaitingResponse> reservationWaiting = response.jsonPath().getList(".", ReservationWaitingResponse.class);
        assertThat(reservationWaiting.size()).isEqualTo(1);
    }
}
