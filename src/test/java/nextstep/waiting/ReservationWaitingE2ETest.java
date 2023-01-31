package nextstep.waiting;

import io.restassured.RestAssured;
import nextstep.AbstractE2ETest;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.schedule.dto.ScheduleRequest;
import nextstep.theme.dto.ThemeRequest;
import nextstep.waiting.dto.ReservationWaitingRequest;
import nextstep.waiting.dto.ReservationWaitingCreatedResponse;
import nextstep.waiting.dto.ReservationWaitingResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ReservationWaitingE2ETest extends AbstractE2ETest {
    private Long reservedScheduleId;
    private Long notReservedscheduleId;

    @BeforeEach
    protected void setUp() {
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
        Long themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, "2023-01-23", "15:00");
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
        reservedScheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);

        ScheduleRequest notReservedScheduleRequest = new ScheduleRequest(themeId, "2023-02-15", "21:00");
        var notReservedScheduleResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(notReservedScheduleRequest)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] notReservedScheduleLocation = notReservedScheduleResponse.header("Location").split("/");
        notReservedscheduleId = Long.parseLong(notReservedScheduleLocation[notReservedScheduleLocation.length - 1]);

        ReservationRequest reservationRequest = new ReservationRequest(reservedScheduleId);
        var reservationResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("예약 대기 신청시 예약이 존재하지 않으면 예약이 생성된다")
    @Test
    void createReservation() {
        ReservationWaitingRequest request = new ReservationWaitingRequest(notReservedscheduleId);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        String header = response.header("Location");
        String url = response.header("Location").split("/")[1];
        String id = response.header("Location").split("/")[2];

        assertThat(url).isEqualTo("reservations");
        assertThat(id).isNotNull();
    }

    @DisplayName("예약 대기 신청시 예약이 존재하면 예약 대기가 생성된다")
    @Test
    void createReservationWaiting() {
        ReservationWaitingRequest request = new ReservationWaitingRequest(reservedScheduleId);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        String url = response.header("Location").split("/")[1];
        String id = response.header("Location").split("/")[2];

        assertThat(url).isEqualTo("reservation-waitings");
        assertThat(id).isNotNull();
    }

    @DisplayName("자신의 예약 대기 목록을 조회한다")
    @Test
    void getReservationWaitings() {
        ReservationWaitingRequest request = new ReservationWaitingRequest(reservedScheduleId);
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());


        List<ReservationWaitingResponse> responses = List.of(RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/reservation-waitings")
                .then().log().all()
                .extract()
                .as(ReservationWaitingResponse[].class));
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getWaitingNumber()).isEqualTo(1);
        assertThat(responses.get(0).getSchedule().getId()).isEqualTo(reservedScheduleId);
        assertThat(responses.get(1).getWaitingNumber()).isEqualTo(2);
        assertThat(responses.get(1).getSchedule().getId()).isEqualTo(reservedScheduleId);
    }

    @DisplayName("예약 대기를 취소한다")
    @Test
    void removeResrevationWaiting() {
        ReservationWaitingRequest request = new ReservationWaitingRequest(reservedScheduleId);
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        Long id = Long.parseLong(response.header("Location").split("/")[2]);

        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/reservation-waitings/"+id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
