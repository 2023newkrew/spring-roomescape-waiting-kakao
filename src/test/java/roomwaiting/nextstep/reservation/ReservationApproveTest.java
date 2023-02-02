package roomwaiting.nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
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

import static roomwaiting.nextstep.reservation.ReservationStatus.APPROVED;
import static roomwaiting.nextstep.reservation.ReservationStatus.NOT_APPROVED;

@SpringBootTest(classes = {RoomEscapeApplication.class})
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ReservationApproveTest extends ReservationCommon {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @DisplayName("Admin 유저만 예약 미승인 상태의 예약을 예약 승인 상태로 변경할 수 있다")
    @Test
    void approveTest(){
        ExtractableResponse<Response> response = requestCreateReservation();
        String location = response.header("Location").split("/")[2];

        List<Reservation> lookUpPre = lookUpReservation().jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpPre.size()).isEqualTo(1);
        Assertions.assertThat(lookUpPre.get(0).getStatus()).isEqualTo(NOT_APPROVED);

        Member member = saveMember(jdbcTemplate, "USER1", "PASS1", "MEMBER");
        String memberToken = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRole());

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(memberToken)
                .body(request)
                .when().patch("/reservations/" + location + "/approve")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .when().patch("/reservations/" + location + "/approve")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        List<Reservation> lookUpPost = lookUpReservation().jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpPost.size()).isEqualTo(1);
        Assertions.assertThat(lookUpPost.get(0).getStatus()).isEqualTo(APPROVED);
    }

    @Test
    void otherStatusApprove(){

    }
}
