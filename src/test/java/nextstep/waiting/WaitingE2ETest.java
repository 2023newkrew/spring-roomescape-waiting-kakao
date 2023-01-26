package nextstep.waiting;

import io.restassured.RestAssured;
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



    @DisplayName("해당 스케줄에 예약이 존재하지 않는 경우")
    @Test
    void createNotExist() {
        var responseReservation = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(new WaitingRequestDTO(scheduleId0))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(responseReservation.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(responseReservation.header("Location")).startsWith("/reservations/");

        var responseWaiting = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(new WaitingRequestDTO(scheduleId0))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(responseWaiting.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(responseWaiting.header("Location")).startsWith("/reservation-waitings/");
    }

}
