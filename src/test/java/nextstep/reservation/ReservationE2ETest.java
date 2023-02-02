package nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.ReservationState;
import nextstep.domain.reservation.ReservationWaiting;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.request.ScheduleRequest;
import nextstep.dto.request.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                .auth().oauth2(adminToken.getAccessToken())
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
                .auth().oauth2(adminToken.getAccessToken())
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void delete() {
        var reservation = createReservation();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("중복 예약을 생성한다")
    @Test
    void createDuplicateReservation() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예약이 없을 때 예약 목록을 조회한다")
    @Test
    void showEmptyReservations() {
        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", DATE)
                .auth().oauth2(adminToken.getAccessToken())
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations).isEmpty();
    }

    @DisplayName("없는 예약을 삭제한다")
    @Test
    void createNotExistReservation() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
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
                .auth().oauth2(userToken.getAccessToken())
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약이 존재하지 않는데 대기를 생성한다")
    @Test
    void createInvalidReservationWaiting() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예약 대기를 생성한다")
    @Test
    void createWaiting() {
        RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all();

        RestAssured.given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("다수 예약 대기 발생")
    @RepeatedTest(20)
    void createWaitingFromMany() throws InterruptedException {

        int threadNum = 50;
        ExecutorService service = Executors.newFixedThreadPool(threadNum);
        CountDownLatch latch = new CountDownLatch(threadNum);

        createReservation();
        for (int i = 0; i < threadNum; i++) {
            service.execute(() -> {
                createReservationWaiting();
                latch.countDown();
            });
        }

        latch.await();
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(userToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<ReservationWaiting> reservations = response.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(reservations).hasSize(threadNum);
    }


    @DisplayName("예약 대기 목록을 조회한다")
    @Test
    void showReservationWaitings() {
        createReservation();
        createReservationWaiting();
        createReservationWaiting();
        createReservationWaiting();
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(userToken.getAccessToken())
                .when().delete("/reservation-waitings/1")
                .then().log().all();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(userToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<ReservationWaiting> reservations = response.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(reservations).hasSize(2);
    }

    @DisplayName("다른 사람이 예약 대기 삭제")
    @Test
    void InvalidDeleteWaiting() {
        createReservation();
        createReservationWaiting();

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken.getAccessToken())
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("예약 대기 삭제")
    @Test
    void deleteWaiting() {
        createReservation();
        createReservationWaiting();

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(userToken.getAccessToken())
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("예약 승인")
    @Test
    void approveReservation() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        Reservation reservation = response.jsonPath().getObject(".", Reservation.class);
        assertThat(reservation.getState()).isEqualTo(ReservationState.ACCEPTED);
    }

    @DisplayName("미승인 예약 취소")
    @Test
    void cancelReservation() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        Reservation reservation = response.jsonPath().getObject(".", Reservation.class);
        assertThat(reservation.getState()).isEqualTo(ReservationState.CANCELED);
    }

    @DisplayName("승인 예약 취소")
    @Test
    void cancelAcceptedReservation() {
        createReservation();
        acceptReservation();

        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        Reservation reservation = response.jsonPath().getObject(".", Reservation.class);
        assertThat(reservation.getState()).isEqualTo(ReservationState.CANCEL_WAITING);
    }

    @DisplayName("다른 사람이 예약 취소")
    @Test
    void cancelAnotherUser() {
        createReservation();

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(userToken.getAccessToken())
                .when().patch("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("예약 거절")
    @Test
    void rejectReservation() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch("admin/reservations/1/reject")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        Reservation reservation = response.jsonPath().getObject(".", Reservation.class);
        assertThat(reservation.getState()).isEqualTo(ReservationState.REJECTED);
    }

    @DisplayName("예약 취소 대기 승인")
    @Test
    void approveCancelWaiting() {
        createReservation();
        acceptReservation();

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch("admin/reservations/1/cancel-approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        Reservation reservation = response.jsonPath().getObject(".", Reservation.class);
        assertThat(reservation.getState()).isEqualTo(ReservationState.CANCELED);
    }

    @DisplayName("취소 대기가 아닌 예약에 대해 취소 대기 승인")
    @Test
    void approveInvalidWaiting() {
        createReservation();

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch("admin/reservations/1/cancel-approve")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> createReservationWaiting() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(userToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
    }

    private void acceptReservation() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> createReservation() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }
}
