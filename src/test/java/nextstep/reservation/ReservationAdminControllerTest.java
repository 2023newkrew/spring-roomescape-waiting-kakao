package nextstep.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import auth.TokenRequest;
import auth.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.error.ErrorCode;
import nextstep.member.MemberRequest;
import nextstep.member.Role;
import nextstep.reservation.domain.ReservationStatus;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.revenue.RevenueStatus;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ReservationAdminControllerTest extends AbstractE2ETest {

    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private ReservationRequest request;
    private Long themeId;
    private Long scheduleId;
    private TokenResponse nonAdminToken;

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

        MemberRequest memberBody = new MemberRequest("normal", PASSWORD, "user", "010-1234-5678", Role.USER);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenBody = new TokenRequest("normal", PASSWORD);
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        nonAdminToken = response.as(TokenResponse.class);
    }

    @DisplayName("관리자는 미승인된 예약을 승인할 수 있다.")
    @Test
    public void approve() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String reservationStatus = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getString("status");
        assertThat(reservationStatus).isEqualTo(ReservationStatus.APPROVED.toString());
    }

    @DisplayName("예약 승인 시 매출 이력이 생성된다.")
    @Test
    public void createRevenueAfterApprove() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String revenueStatus = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getString("revenue.status");
        assertThat(revenueStatus).isEqualTo(RevenueStatus.OK.toString());
    }

    @DisplayName("예약 승인 전에는 매출 이력이 존재하지 않는다.")
    @Test
    public void noRevenueBeforeApprove() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when // then
        Object revenue = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().get("revenue");
        assertThat(revenue).isNull();
    }

    @DisplayName("일반 유저는 예약을 승인할 수 없다.")
    @Test
    public void NonAuthorizedApprove() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when // then
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract();
    }

    @DisplayName("관리자는 승인된 예약을 다시 승인할 수 없다.")
    @Test
    public void duplicatedApprove() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_ALREADY_APPROVED.getMessage());
    }

    @DisplayName("관리자는 취소된 예약을 승인할 수 없다.")
    @Test
    public void cantApproveCancelledReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_ALREADY_CANCELLED.getMessage());
    }

    @DisplayName("관리자는 취소 대기중인 예약을 승인할 수 없다.")
    @Test
    public void cantApproveReservationWaitCancel() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_WAIT_CANCEL.getMessage());
    }

    @DisplayName("관리자는 거절된 예약을 승인할 수 없다.")
    @Test
    public void cantApproveRefusedReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_ALREADY_REFUSED.getMessage());
    }

    @DisplayName("관리자는 미승인된 예약을 거절할 수 있다.")
    @Test
    public void refuseUnapprovedReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String reservationStatus = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().get("status");
        assertThat(reservationStatus).isEqualTo(ReservationStatus.REFUSED.toString());
    }

    @DisplayName("관리자는 승인된 예약을 거절할 수 있다.")
    @Test
    public void refuseApprovedReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when
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
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String reservationStatus = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().get("status");
        assertThat(reservationStatus).isEqualTo(ReservationStatus.REFUSED.toString());
    }

    @DisplayName("승인된 예약 거절 시 매출 이력의 상태가 환불로 변경된다.")
    @Test
    public void refundRevenueAfterRefuse() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when
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
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String revenueStatus = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getString("revenue.status");
        assertThat(revenueStatus).isEqualTo(RevenueStatus.REFUND.toString());
    }

    @DisplayName("미승인 예약 거절 시 매출 이력이 생기지 않는다.")
    @Test
    public void noRevenueAfterRefuseUnapproved() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        Object revenue = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().get("revenue");
        assertThat(revenue).isNull();
    }

    @DisplayName("일반 유저는 예약을 거절할 수 없다.")
    @Test
    public void UnauthorizedRefuse() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when // then
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract();
    }

    @DisplayName("관리자는 취소된 예약을 거절할 수 없다.")
    @Test
    public void cantRefuseCancelledReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_ALREADY_CANCELLED.getMessage());
    }

    @DisplayName("관리자는 취소 대기중인 예약을 거절할 수 없다.")
    @Test
    public void cantRefuseReservationWaitCancel() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_WAIT_CANCEL.getMessage());
    }

    @DisplayName("관리자는 거절된 예약을 다시 거절할 수 없다.")
    @Test
    public void duplicatedRefuse() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_ALREADY_REFUSED.getMessage());
    }


    @DisplayName("유저는 미승인된 예약을 취소할 수 있다.")
    @Test
    public void cancelUnapprovedReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String reservationStatus = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getString("status");
        assertThat(reservationStatus).isEqualTo(ReservationStatus.CANCELLED.toString());
    }

    @DisplayName("유저는 승인된 예약을 취소 신청할 수 있다.")
    @Test
    public void cancelApprovedReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String reservationStatus = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getString("status");
        assertThat(reservationStatus).isEqualTo(ReservationStatus.CANCEL_WAITING.toString());
    }

    @DisplayName("유저는 취소된 예약을 다시 취소할 수 없다.")
    @Test
    public void duplicatedCancel() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_ALREADY_CANCELLED.getMessage());
    }

    @DisplayName("유저는 취소 대기중인 예약을 다시 취소 신청할 수 없다.")
    @Test
    public void cantCancelReservationWaitCancel() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_WAIT_CANCEL.getMessage());
    }

    @DisplayName("유저는 거절된 예약을 취소할 수 없다.")
    @Test
    public void cantCancelRefusedReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_ALREADY_REFUSED.getMessage());
    }

    @DisplayName("관리자는 취소 대기중인 예약을 취소 승인할 수 있다.")
    @Test
    public void approveCancel() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/cancel-approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String status = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getString("status");
        assertThat(status).isEqualTo(ReservationStatus.CANCELLED.toString());
    }

    @DisplayName("취소 승인 시 매출 이력의 상태가 환불로 변경된다.")
    @Test
    public void refundRevenueAfterApproveCancel() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/cancel-approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        String revenueStatus = RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getString("revenue.status");
        assertThat(revenueStatus).isEqualTo(RevenueStatus.REFUND.toString());
    }

    @DisplayName("관리자는 취소된 예약을 취소 승인할 수 없다.")
    @Test
    public void cantApproveCancelOfCancelledReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(nonAdminToken.getAccessToken())
                .when().post("/reservations/1/cancel")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/cancel-approve")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_ALREADY_CANCELLED.getMessage());
    }

    @DisplayName("관리자는 미승인된 예약을 취소 승인할 수 없다.")
    @Test
    public void cantApproveCancelOfUnapprovedReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/cancel-approve")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_CANT_BE_CANCELLED.getMessage());
    }

    @DisplayName("관리자는 승인된 예약을 취소 승인할 수 없다.")
    @Test
    public void cantApproveCancelOfApprovedReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/cancel-approve")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_ALREADY_APPROVED.getMessage());
    }

    @DisplayName("관리자는 거절된 예약을 취소 승인할 수 없다.")
    @Test
    public void cantApproveCancelOfRefusedReservation() {
        // given
        createReservation(nonAdminToken.getAccessToken());
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/refuse")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // when // then
        String message = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().post("/admin/reservations/1/cancel-approve")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .jsonPath().getString("message");
        assertThat(message).isEqualTo(ErrorCode.RESERVATION_ALREADY_REFUSED.getMessage());
    }

    private ExtractableResponse<Response> createReservation(String loginToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(loginToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }
}