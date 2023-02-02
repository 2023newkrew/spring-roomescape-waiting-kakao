package nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
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
        Long scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);

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
                .auth().oauth2("other-token")
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("나의 예약 목록을 조회할 수 있다")
    @Test
    void showMyReservation() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(1);
    }

    @DisplayName("나의 예약 목록이 없는 경우 빈 List가 출력된다")
    @Test
    void showMyEmptyReservation() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(0);
    }

    @DisplayName("관리자가 예약을 승인한다")
    @Test
    void approveReservation() {
        createReservation();

        RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch("/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("일반 사용자는 예약을 승인할 수 없다")
    @Test
    void normalUserCannotApproveReservation() {
        createReservation();

        RestAssured
                .given().log().all()
                .auth().oauth2(normalToken.getAccessToken())
                .when().patch("/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("관리자가 예약 취소 신청한다")
    @Test
    void adminCancelReservation() {
        createReservation();

        RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("일반 사용자가 예약 취소 신청한다")
    @Test
    void normalUserCancelReservation() {
        createReservation();

        RestAssured
                .given().log().all()
                .auth().oauth2(normalToken.getAccessToken())
                .when().patch("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("관리자가 예약 취소를 승인한다")
    @Test
    void adminCancelApporveReservation() {
        createReservation();

        RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().patch("/reservations/1/cancel-approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("일반 사용자가 예약 취소를 승인할 수 없다")
    @Test
    void normalUserCannotCancelApproveReservation() {
        createReservation();

        RestAssured
                .given().log().all()
                .auth().oauth2(normalToken.getAccessToken())
                .when().patch("/reservations/1/cancel-approve")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
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
