package nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import nextstep.waiting.dto.response.ReservationWaitingResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationE2ETest extends AbstractE2ETest {

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
                .auth().oauth2(adminToken.getAccessToken())
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
                .auth().oauth2(adminToken.getAccessToken())
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

    @DisplayName("예약을 생성한다")
    @Test
    void create() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("대기예약을 생성한다")
    @Test
    void create_waiting() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        var waitingResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(waitingResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("비로그인 사용자가 예약을 생성한다")
    @Test
    void createWithoutLogin() {
        var response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약을 조회한다")
    @Test
    void show() {
        createReservation();

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

    @DisplayName("내 예약을 조회한다")
    @Test
    void show_my_reservations() {
        var otherUserToken = generateNewMemberToken("otherUser", "password", "USER");

        RestAssured
                .given().log().all()
                .auth().oauth2(otherUserToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

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
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(0);
    }

    @DisplayName("내 예약대기를 조회한다")
    @Test
    void show_my_reservation_waitings() {
        var otherUserToken = generateNewMemberToken("otherUser", "password", "USER");

        RestAssured
                .given().log().all()
                .auth().oauth2(otherUserToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

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
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<ReservationWaitingResponseDto> dtos = response.jsonPath().getList(".", ReservationWaitingResponseDto.class);
        assertThat(dtos.size()).isEqualTo(1);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void delete() {
        Long reservationId = createReservationAndGetId();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().patch("/reservations/" + reservationId + "/cancel")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @DisplayName("예약대기를 삭제한다")
    @Test
    void deleteWaiting() {
        createReservation();
        var waitingReservation = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        long waitingReservationId = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get(waitingReservation.header("Location"))
                .then().log().all()
                .extract()
                .jsonPath().getList(".", ReservationWaitingResponseDto.class).get(0).getId();

        var waitingResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservation-waitings/" + waitingReservationId)
                .then().log().all()
                .extract();

        assertThat(waitingResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("중복 예약을 생성한다")
    @Test
    void createDuplicateReservation() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("예약이 없을 때 예약 목록을 조회한다")
    @Test
    void showEmptyReservations() {
        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", DATE)
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(0);
    }

    @DisplayName("없는 예약을 삭제한다")
    @Test
    void deleteNotExistReservation() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("다른 사람이 예약을삭제한다")
    @Test
    void deleteReservationOfOthers() {
        Long reservationId = createReservationAndGetId();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2("other-token")
                .when().patch("/reservations/" + reservationId + "/cancel")
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

    @DisplayName("예약을 승인한다.")
    @Test
    void reservationApprove() {
        Long reservationId = createReservationAndGetId();
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/reservations/" + reservationId + "/approve")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("관리자가 아닌 사용자의 예약 승인은 실패한다.")
    @Test
    void should_canNotReservationApprove_when_notAdmin() {
        Long reservationId = createReservationAndGetId();
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/reservations/" + reservationId + "/approve")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private Long createReservationAndGetId() {
        var reservation = createReservation();

        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get(reservation.header("Location"))
                .then().log().all()
                .extract()
                .jsonPath().getList(".", Reservation.class).get(0).getId();
    }
}
