package roomescape.nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomescape.nextstep.AbstractE2ETest;
import roomescape.nextstep.schedule.ScheduleRequest;
import roomescape.nextstep.theme.ThemeRequest;
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

    @DisplayName("예약을 삭제한다")
    @Test
    void delete() {
        var reservation = createReservation();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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
    void createNotExistReservation() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("다른 사람이 예약을삭제한다")
    @Test
    void deleteReservationOfOthers() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2("other-token")
                .when().delete("/reservations/1")
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

    @DisplayName("예약이 없을 때 예약 대기 신청") // reservation/1
    @Test
    void reservationWaiting() {
        var waitingResponse = createReservation();

        assertThat(waitingResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(waitingResponse.header("Location")).isEqualTo("/reservations/1");

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

    @DisplayName("예약이 있을 때 예약 대기 신청") //reservations-waiting/1
    @Test
    void reservationWaiting2() {
        create(); //예약 생성
        var waitingResponse = createReservation();

        assertThat(waitingResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(waitingResponse.header("Location")).isEqualTo("/reservations-waitings/2");
    }

    @DisplayName("내 예약대기 조회")
    @Test
    void findMyReservationsWaitings() {
        createReservation();
        var waitingResponse = createReservation();

        assertThat(waitingResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations-waitings/mine")
                .then().log().all()
                .extract();

        List<ReservationWaiting> reservationWaitings = response.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(reservationWaitings).hasSize(1);
    }

    @DisplayName("내 예약 조회")
    @Test
    void findMyReservations() {
        var waitingResponse = createReservation();

        assertThat(waitingResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        List<Reservation> reservation = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservation).hasSize(1);
    }


    @DisplayName("예약 대기 취소")
    @Test
    void deleteWaiting() {
        createReservation();
        var reservation = createReservation();
        assertThat(reservation.header("Location")).isEqualTo("/reservations-waitings/2");

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("없는 예약 대기를 취소한다")
    @Test
    void createNotExistWaiting() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservations-waitings/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("다른 사람의 예약대기를 삭제한다")
    @Test
    void deleteWaitingOfOthers() {
        createReservation();
        createReservation();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2("other-token")
                .when().delete("/reservations-waitings/2")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약이 삭제되면 예약 대기 중 대기 번호가 가장 작은 예약 대기가 예약이 된다")
    @Test
    void updateReservationWaiting() {
        createReservation(); //예약, 번호 1번
        createReservation(); //예약 대기, 번호 2번
        createReservation(); //예약 대기, 번호 3번

        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations-waitings/mine")
                .then().log().all()
                .extract();

        List<ReservationWaiting> reservationWaitings = response.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(reservationWaitings).hasSize(1);

        var response2 = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        List<Reservation> reservation = response2.jsonPath().getList(".", Reservation.class);
        assertThat(reservation.get(0).getId()).isEqualTo(2);
    }
}
