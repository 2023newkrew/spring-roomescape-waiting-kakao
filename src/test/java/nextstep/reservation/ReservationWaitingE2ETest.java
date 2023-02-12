package nextstep.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.AbstractE2ETest;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationStatus;
import nextstep.reservation.domain.ReservationWaiting;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ReservationWaitingE2ETest extends AbstractE2ETest {
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private ReservationRequest request;
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
    }

    @DisplayName("예약이 되지 않은 스케줄에 예약 대기 신청을 하면 예약이 된다.")
    @Test
    void requestWaitingNonReservedSchedule() {
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

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

    @DisplayName("예약이 되어있는 스케줄에 예약 대기 신청을 할 수 있다.")
    @Test
    void requestWaitingReservedSchedule() {
        // given
        createReservation();

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        // then
        var responseReserve = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservations")
                .then().log().all()
                .extract();
        List<Reservation> reservations = responseReserve.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(1);

        var responseWaiting = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();
        List<ReservationWaiting> waitingList = responseWaiting.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(waitingList.size()).isEqualTo(1);
    }

    @DisplayName("자신이 예약 대기 중인 스케줄엔 대기를 요청할 수 없다.")
    @Test
    void requestDuplicatedWaiting() {
        // given
        createReservation();

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        // then
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }

    @DisplayName("자신의 예약 대기를 취소할 수 있다")
    @Test
    void deleteWaiting() {
        // given
        createReservation();

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .extract();

        // then
        var responseReserve = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservations")
                .then().log().all()
                .extract();
        List<Reservation> reservations = responseReserve.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(1);

        var responseWaiting = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();
        List<ReservationWaiting> waitingList = responseWaiting.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(waitingList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("없는 예약 대기의 경우 취소할 수 없다.")
    void deleteNonExistentWaiting() {
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }

    @DisplayName("자신의 얘약 대기가 아닌 경우 취소할 수 없다")
    @Test
    void deleteWaitingByOtherUser() {
        // given
        createReservation();
        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
        // then
        RestAssured
                .given().log().all()
                .auth().oauth2("other-token")
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    @Test
    @DisplayName("로그인을 하지 않은 상태로 예약 대기 조회를 할 수 없다.")
    void lookupWaitingWithNoneAuthority() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    @Test
    @DisplayName("로그인을 하지 않은 상태로 예약 대기 신청을 할 수 없다.")
    void createWaitingWithNoneAuthority() {
        RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    @Test
    @DisplayName("로그인을 하지 않은 상태로 예약 대기를 삭제 할 수 없다")
    void deleteWaitingWIthNoneAuthority() {
        // given
        createReservation();
        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
        // then
        RestAssured
                .given().log().all()
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    @DisplayName("미승인된 예약 거절 시 다음 대기가 예약이 된다.")
    @Test
    public void refuseUnapprovedAndPassNext() {
        // given
        createReservation();
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String reservationStatus1 = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().get("status");
        assertThat(reservationStatus1).isEqualTo(ReservationStatus.REFUSED.toString());
        String reservationStatus2 = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/2")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().get("status");
        assertThat(reservationStatus2).isEqualTo(ReservationStatus.UNAPPROVED.toString());
    }

    @DisplayName("승인된 예약 거절 시 다음 대기가 예약이 된다.")
    @Test
    public void refuseApprovedAndPassNext() {
        // given
        createReservation();
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String reservationStatus1 = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().get("status");
        assertThat(reservationStatus1).isEqualTo(ReservationStatus.REFUSED.toString());
        String reservationStatus2 = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/2")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().get("status");
        assertThat(reservationStatus2).isEqualTo(ReservationStatus.UNAPPROVED.toString());
    }

    @DisplayName("미승인된 예약 취소 시 다음 대기가 예약이 된다.")
    @Test
    public void cancelUnapprovedAndPassNext() {
        // given
        createReservation();
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String reservationStatus1 = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().get("status");
        assertThat(reservationStatus1).isEqualTo(ReservationStatus.CANCELLED.toString());
        String reservationStatus2 = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/2")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().get("status");
        assertThat(reservationStatus2).isEqualTo(ReservationStatus.UNAPPROVED.toString());
    }

    @DisplayName("승인된 예약 취소 시 다음 대기가 예약이 되지 않는다.")
    @Test
    public void cancelApprovedAndNotPassNext() {
        // given
        createReservation();
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String reservationStatus1 = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().get("status");
        assertThat(reservationStatus1).isEqualTo(ReservationStatus.CANCEL_WAITING.toString());

        var responseWaiting = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();
        List<ReservationWaiting> waitingList = responseWaiting.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(waitingList.size()).isEqualTo(1);
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

}