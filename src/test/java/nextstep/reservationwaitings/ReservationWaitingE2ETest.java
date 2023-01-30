package nextstep.reservationwaitings;

import io.restassured.RestAssured;
import nextstep.AbstractE2ETest;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.Reservation;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ReservationWaitingE2ETest extends AbstractE2ETest {

    @DisplayName("예약 대기 신청을 한다.")
    @Test
    void createReservationWaiting() {
        long themeId = createTheme("테마이름", "테마설명", 22000);
        long scheduleId = createSchedule(themeId, "2018-10-22", "13:00");
        createReservation(scheduleId);

        var request = new ReservationWaitingRequest(scheduleId);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("예약 대기가 없을 때 예약을 한다.")
    @Test
    void createReservationIfNoWaiting() {
        long themeId = createTheme("테마이름", "테마설명", 22000);
        long scheduleId = createSchedule(themeId, "2018-10-22", "13:00");

        var request = new ReservationWaitingRequest(scheduleId);

        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", "2018-10-22")
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("예약 대기 목록을 조회한다.")
    @Test
    void getReservationWaitings() {
        long themeId1 = createTheme("테마이름1", "테마설명1", 22000);
        long themeId2 = createTheme("테마이름2","테마설명2", 44000);

        long scheduleId1 = createSchedule(themeId1, "2018-10-22", "13:00");
        long scheduleId2 = createSchedule(themeId2, "2018-10-22", "13:00");
        createReservation(scheduleId1);
        createReservation(scheduleId2);

        long waitingId1 = createReservationWaiting(scheduleId1);
        long waitingId2 = createReservationWaiting(scheduleId1);
        long waitingId3 = createReservationWaiting(scheduleId2);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<ReservationWaitings> reservationWaitings = response.jsonPath().getList(".", ReservationWaitings.class);
        assertThat(reservationWaitings)
                .extracting(ReservationWaitings::getId, r -> r.getSchedule().getId(), ReservationWaitings::getWaitNum)
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        tuple(waitingId1, scheduleId1, 1L),
                        tuple(waitingId2, scheduleId1, 2L),
                        tuple(waitingId3, scheduleId2, 1L)
                );
    }

    private long createTheme(String themeName, String themeDesc, int themePrice) {
        ThemeRequest themeRequest = new ThemeRequest(themeName, themeDesc, themePrice);
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
        return Long.parseLong(themeLocation[themeLocation.length - 1]);
    }

    private long createSchedule(Long themeId, String date, String time) {
        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, date, time);
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
        return Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);
    }

    private long createReservation(long scheduleId) {
        var request = new ReservationRequest(scheduleId);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
        String[] reservationLocation = response.header("Location").split("/");
        return Long.parseLong(reservationLocation[reservationLocation.length - 1]);
    }

    private long createReservationWaiting(long scheduleId) {
        var request = new ReservationWaitingRequest(scheduleId);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
        String[] location = response.header("Location").split("/");
        return Long.parseLong(location[location.length - 1]);
    }
}
