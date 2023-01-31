package nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.DatabaseCleaner;
import nextstep.domain.dto.request.ReservationRequest;
import nextstep.domain.dto.response.ReservationResponse;
import nextstep.domain.dto.request.ScheduleRequest;
import nextstep.domain.dto.request.ThemeRequest;
import nextstep.domain.enumeration.ReservationStatus;
import nextstep.domain.persist.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
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
        themeId = super.createTheme();
        scheduleId = super.createSchedule(themeId);
        request = new ReservationRequest(scheduleId);
    }

    @DisplayName("예약을 생성한다")
    @Test
    void Should_CreateReservation_When_Request() {
        var response = given().log().all()
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
    void Should_ThrowUnAuthorized_When_IfUserIsNotLoggedIn() {
        var response = given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약을 조회한다")
    @Test
    void Should_GetAllReservationInfo_When_Request() {
        createReservation();

        var response = given().log().all()
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
    void Should_GetMyReservationInfo_When_Request() {
        createReservation();

        var response = given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservationResponses = response.jsonPath().getList(".", ReservationResponse.class);
        assertThat(reservationResponses.size()).isEqualTo(1);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void Should_DeleteReservation_When_Request() {
        var reservation = createReservation();

        var response = given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("중복 예약을 생성한다")
    @Test
    void Should_ThrowBadRequest_When_RequestCreateDuplicateReservation() {
        createReservation();

        var response = given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예약이 없을 때 예약 목록을 조회한다")
    @Test
    void Should_ReturnEmptyList_When_IfNoReservationExists() {
        var response = given().log().all()
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
    void Should_ThrowNotFound_When_IfAttemptToDeleteNotExists() {
        var response = given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("다른 사람이 예약을 삭제한다")
    @Test
    void Should_ThrowUnAuthorized_When_IfAttemptToDeleteNotMine() {
        createReservation();

        var response = given().log().all()
                .auth().oauth2("other-token")
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("관리자가 아니면 예약을 승인할 수 없다.")
    void Should_ThrowUnAuthorized_When_IfAttemptToApproveNotAdmin() {
        createReservation();

        given().
                auth().oauth2("other-token").
        when().
                patch("/reservations/1/approve").
        then().
                assertThat().
                statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("관리자는 예약을 승인할 수 있다.")
    void Should_Approve_When_IfAttemptToApproveAdmin() {
        createReservation();

        given().
                auth().oauth2(token.getAccessToken()).
        when().
                patch("/reservations/1/approve").
        then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        var response = given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservationResponses = response.jsonPath().getList(".", ReservationResponse.class);
        assertThat(reservationResponses.get(0).getStatus()).isEqualTo(ReservationStatus.APPROVED);
    }

    @Test
    @DisplayName("승인 대기 상태가 아닌 예약은 승인할 수 없다.")
    void Should_ThrowBadRequest_When_IfAttemptToApproveInvalidStatus() {
        createReservation();
        given().
                auth().oauth2(token.getAccessToken()).
        when().
                patch("/reservations/1/approve").
        then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        given().
                auth().oauth2(token.getAccessToken()).
        when().
                patch("/reservations/1/approve").
        then().
                assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("미승인 상태의 예약은 취소 시 취소 상태가 된다.")
    void Should_Cancel_When_IfAttemptToCancelIsNotApproved() {
        createReservation();

        given().
                auth().oauth2(token.getAccessToken()).
        when().
                patch("/reservations/1/cancel").
        then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        var response = given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservationResponses = response.jsonPath().getList(".", ReservationResponse.class);
        assertThat(reservationResponses.get(0).getStatus()).isEqualTo(ReservationStatus.CANCELED);
    }

    @Test
    @DisplayName("승인 상태의 예약은 취소 시 취소 상태가 된다.")
    void Should_WaitCancel_When_IfAttemptToCancelIsApproved() {
        createReservation();

        given().
                auth().oauth2(token.getAccessToken()).
        when().
                patch("/reservations/1/approve").
        then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        given().
                auth().oauth2(token.getAccessToken()).
        when().
                patch("/reservations/1/cancel").
        then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        var response = given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservationResponses = response.jsonPath().getList(".", ReservationResponse.class);
        assertThat(reservationResponses.get(0).getStatus()).isEqualTo(ReservationStatus.WAIT_CANCEL);
    }
    @Test
    @DisplayName("예약 취소 가능 상태가 아닌 예약은 취소할 수 없다.")
    void Should_ThrowBadRequest_When_IfAttemptToCancelInvalidStatus() {
        createReservation();

        given().
                auth().oauth2(token.getAccessToken()).
        when().
                patch("/reservations/1/cancel").
        then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        given().
                auth().oauth2(token.getAccessToken()).
        when().
                patch("/reservations/1/cancel").
        then().
                assertThat().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> createReservation() {
        return given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }
}
