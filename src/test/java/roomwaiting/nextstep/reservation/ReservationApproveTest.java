package roomwaiting.nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import roomwaiting.AcceptanceTestExecutionListener;
import roomwaiting.auth.token.JwtTokenProvider;
import roomwaiting.nextstep.RoomEscapeApplication;
import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.domain.Reservation;

import java.util.List;

import static roomwaiting.nextstep.reservation.ReservationStatus.*;

@SpringBootTest(classes = {RoomEscapeApplication.class})
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ReservationApproveTest extends ReservationCommon {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp(){
        super.setUp();
    }

    @DisplayName("Admin 유저만 예약 미승인 상태의 예약을 예약 승인 상태로 변경할 수 있다")
    @Test
    void approveTest() {
        ExtractableResponse<Response> response = requestCreateReservation(token.getAccessToken());
        String location = response.header("Location").split("/")[2];

        List<Reservation> lookUpPre = lookUpReservation(token.getAccessToken()).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpPre.size()).isEqualTo(1);
        Assertions.assertThat(lookUpPre.get(0).getStatus()).isEqualTo(NOT_APPROVED);

        Member member = saveMember(jdbcTemplate, "USER1", "PASS1", "MEMBER");
        String memberToken = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRole());
        ExtractableResponse<Response> memberRequest = requestApprove(location, memberToken);
        Assertions.assertThat(memberRequest.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        ExtractableResponse<Response> adminRequest = requestApprove(location, token.getAccessToken());
        Assertions.assertThat(adminRequest.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Reservation> lookUpPost = lookUpReservation(token.getAccessToken()).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpPost.size()).isEqualTo(1);
        Assertions.assertThat(lookUpPost.get(0).getStatus()).isEqualTo(APPROVED);
    }

    @DisplayName("사용자가 예약을 취소할 때 예약이 미승인 상태면 예약 취소 상태가 된다")
    @Test
    void memberReserveNotApproveCancel() {
        Member member = saveMember(jdbcTemplate, "MEMBER1", "PASS1", "MEMBER");
        String memberToken = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRole());
        ExtractableResponse<Response> response = requestCreateReservation(memberToken);
        String location = response.header("Location").split("/")[2];

        List<Reservation> lookUpPre = lookUpReservation(memberToken).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpPre.size()).isEqualTo(1);
        Assertions.assertThat(lookUpPre.get(0).getStatus()).isEqualTo(NOT_APPROVED);

        ExtractableResponse<Response> memberCancel = cancelReservation(location, memberToken);
        Assertions.assertThat(memberCancel.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Reservation> lookUpPost = lookUpReservation(memberToken).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpPost.size()).isEqualTo(1);
        Assertions.assertThat(lookUpPost.get(0).getStatus()).isEqualTo(CANCEL);
    }

    @DisplayName("사용자가 예약을 취소할 때 예약이 승인 상태면 예약 취소 대기 상태가 된다")
    @Test
    void memberReserveApproveCancel() {
        Member member = saveMember(jdbcTemplate, "MEMBER1", "PASS1", "MEMBER");
        String memberToken = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRole());
        ExtractableResponse<Response> response = requestCreateReservation(memberToken);
        String location = response.header("Location").split("/")[2];

        requestApprove(location, token.getAccessToken());
        List<Reservation> lookUpPre = lookUpReservation(token.getAccessToken()).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpPre.size()).isEqualTo(1);
        Assertions.assertThat(lookUpPre.get(0).getStatus()).isEqualTo(APPROVED);

        ExtractableResponse<Response> memberCancel = cancelReservation(location, memberToken);
        Assertions.assertThat(memberCancel.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Reservation> lookUpPost = lookUpReservation(memberToken).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpPost.size()).isEqualTo(1);
        Assertions.assertThat(lookUpPost.get(0).getStatus()).isEqualTo(CANCEL_WAIT);
    }

    @DisplayName("관리자가 예약을 거절할 때 예약이 미승인 상태면 예약 거절 상태가 된다")
    @Test
    void adminNonApproveReserveCancel() {
        Member member = saveMember(jdbcTemplate, "MEMBER", "PASS1", "MEMBER");
        String memberToken = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRole());
        ExtractableResponse<Response> createReservation = requestCreateReservation(memberToken);
        String location = createReservation.header("Location").split("/")[2];

        ExtractableResponse<Response> notApproveCancel = cancelReservation(location, token.getAccessToken());
        Assertions.assertThat(notApproveCancel.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Reservation> lookUpNotApprove = lookUpReservation(memberToken).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpNotApprove.get(0).getStatus()).isEqualTo(DECLINE);
    }

    @DisplayName("관리자가 예약을 거절할 때 예약이 승인 상태면 예약 거절 상태가 된다")
    @Test
    void adminApproveReserveCancel() {
        Member member = saveMember(jdbcTemplate, "MEMBER", "PASS1", "MEMBER");
        String memberToken = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRole());
        ExtractableResponse<Response> createReservation = requestCreateReservation(memberToken);
        String location = createReservation.header("Location").split("/")[2];
        requestApprove(location, token.getAccessToken());

        ExtractableResponse<Response> approveCancel = cancelReservation(location, token.getAccessToken());
        Assertions.assertThat(approveCancel.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Reservation> lookUpApprove = lookUpReservation(memberToken).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpApprove.get(0).getStatus()).isEqualTo(DECLINE);
    }

    @DisplayName("예약 취소 대기 상태의 예약을 관리자가 취소승인 하는경우 예약 취소 상태가 된다")
    @Test
    void adminCancelApproveTest(){
        Member member = saveMember(jdbcTemplate, "MEMBER1", "PASS1", "MEMBER");
        String memberToken = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRole());
        ExtractableResponse<Response> response = requestCreateReservation(memberToken);
        String location = response.header("Location").split("/")[2];

        requestApprove(location, token.getAccessToken());
        cancelReservation(location, memberToken);

        ExtractableResponse<Response> result = requestCancelApprove(location, token.getAccessToken());
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Reservation> lookUpReservation = lookUpReservation(memberToken).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpReservation.get(0).getStatus()).isEqualTo(CANCEL);
    }

    private ExtractableResponse<Response> requestApprove(String location, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(request)
                .when().patch("/reservations/" + location + "/approve")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> cancelReservation(String location, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(request)
                .when().patch("/reservations/" + location + "/cancel")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestCancelApprove(String location, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(request)
                .when().patch("/reservations/" + location + "/cancel-approve")
                .then().log().all()
                .extract();
    }
}
