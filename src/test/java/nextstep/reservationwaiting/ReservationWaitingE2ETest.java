package nextstep.reservationwaiting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.AbstractE2ETest;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ReservationWaitingE2ETest extends AbstractE2ETest {
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private ReservationWaitingRequest request;
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

        request = new ReservationWaitingRequest(
                scheduleId
        );

        reservationRequest = new ReservationRequest(
                scheduleId
        );
    }

    @DisplayName("예약 대기를 생성한다")
    @Test
    void createWithReservation() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location").contains("/reservation-waitings/")).isEqualTo(true);
    }

    @DisplayName("예약이 없을 때 예약 대기를 생성하면 예약이 생성됨")
    @Test
    void createWithoutReservation() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location").contains("/reservations/")).isEqualTo(true);
    }

    @DisplayName("비로그인 사용자가 예약 대기를 생성한다")
    @Test
    void createWithoutLogin() {
        var response = RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("내 예약 대기 목록을 조회한다")
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

        List<ReservationWaiting> reservationWaitings = response.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(reservationWaitings.size()).isEqualTo(1);
    }

    @DisplayName("예약 대기를 삭제한다")
    @Test
    void delete() {
        createReservation();
        var reservation = createReservationWaiting();

        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("deletedReservationWaitingCount", is(1));
    }

    @DisplayName("다음 예약 대기자를 추가한다")
    @Test
    void createDuplicateReservation() {
        createReservationWaiting();

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

    @DisplayName("예약이 없을 때 내 예약 대기 목록을 조회한다")
    @Test
    void showEmptyReservations() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<ReservationWaiting> reservationWaitings = response.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(reservationWaitings.size()).isEqualTo(0);
    }

    @DisplayName("없는 예약 대기를 삭제한다")
    @Test
    void createNotExistReservation() {
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("deletedReservationWaitingCount", is(0));
    }

    @DisplayName("다른 사람이 예약 대기를 삭제한다")
    @Test
    void deleteReservationOfOthers() {
        createReservationWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2("other-token")
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
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

    private ExtractableResponse<Response> createReservationWaiting() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
    }
}
