package nextstep.reservationwaitings;

import io.restassured.RestAssured;
import nextstep.AbstractE2ETest;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

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
}
