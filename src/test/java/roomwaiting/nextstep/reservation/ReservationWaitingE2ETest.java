package roomwaiting.nextstep.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomwaiting.auth.token.JwtTokenProvider;
import roomwaiting.nextstep.RoomEscapeApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import roomwaiting.AcceptanceTestExecutionListener;
import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.domain.Reservation;
import roomwaiting.nextstep.reservation.domain.ReservationWaiting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@SpringBootTest(classes = {RoomEscapeApplication.class})
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ReservationWaitingE2ETest extends ReservationCommon {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        super.setUp();
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

        List<Reservation> reservations = lookUpReservation(token.getAccessToken()).jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("예약이 되어있는 스케줄에 예약 대기 신청을 할 수 있다.")
    @Test
    void requestWaitingReservedSchedule(){
        // given
        requestCreateReservation(token.getAccessToken());

        // when
        Member otherUser = saveMember(jdbcTemplate, "member2", "pass1", "ADMIN");
        String otherToken = jwtTokenProvider.createToken(String.valueOf(otherUser.getId()), otherUser.getRole());
        createReservationWaiting(otherToken);

        List<Reservation> reservations = lookUpReservation(token.getAccessToken()).jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(1);

        var responseWaiting = RestAssured
                .given().log().all()
                .auth().oauth2(otherToken)
                .param("themeId", themeId)
                .param("date", TEST_DATE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();
        List<ReservationWaiting> waitingList = responseWaiting.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(waitingList.size()).isEqualTo(1);
    }

    @DisplayName("자신의 예약 대기를 취소할 수 있다")
    @Test
    void deleteWaiting(){
        // given
        requestCreateReservation(token.getAccessToken());

        // when
        createReservationWaiting(token.getAccessToken());

        var responseWaitingPre = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .param("themeId", themeId)
                .param("date", TEST_DATE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();
        List<ReservationWaiting> waitingListPre = responseWaitingPre.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(waitingListPre.size()).isEqualTo(1);
        // then
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservation-waitings/" + waitingListPre.get(0).getId())
                .then().log().all()
                .extract();

        var responseWaitingPost = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .param("themeId", themeId)
                .param("date", TEST_DATE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();
        List<ReservationWaiting> waitingListPost = responseWaitingPost.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(waitingListPost.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("없는 예약 대기의 경우 취소할 수 없다.")
    void deleteNonExistentWaiting() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }

    @DisplayName("자신의 얘약 대기가 아닌 경우 취소할 수 없다")
    @Test
    void deleteWaitingByOtherUser(){
        // given
        requestCreateReservation(token.getAccessToken());
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
                .statusCode(HttpStatus.FORBIDDEN.value())
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
    @DisplayName("로그인을 하지 않은 상태로 예약 삭제를 할 수 없다")
    void deleteWaitingWIthNoneAuthority(){
        // given
        requestCreateReservation(token.getAccessToken());
        // when
        RestAssured
                .given().log().all()
                .body(request)
                .auth().oauth2(token.getAccessToken())
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

    @DisplayName("waitNum은 1부터 제공하며 이후에는 최대값+1 값을 제공한다")
    @Test
    void waitingNumberTest(){
        Member member1 = saveMember(jdbcTemplate, "USER1", "PASS1", "ADMIN");
        String token1 = jwtTokenProvider.createToken(String.valueOf(member1.getId()), member1.getRole());
        Member member2 = saveMember(jdbcTemplate, "USER2", "PASS1", "ADMIN");
        String token2 = jwtTokenProvider.createToken(String.valueOf(member2.getId()), member2.getRole());
        Member member3 = saveMember(jdbcTemplate, "USER3", "PASS1", "ADMIN");
        String token3 = jwtTokenProvider.createToken(String.valueOf(member3.getId()), member3.getRole());

        ExtractableResponse<Response> reservation = createReservationWaiting(token1);
        Assertions.assertThat(reservation.header("Location").split("/")[1]).isEqualTo("reservations"); // not reserved

        // waiting 1,2 reserved
        ExtractableResponse<Response> reservationWaiting1 = createReservationWaiting(token2);
        Assertions.assertThat(reservationWaiting1.header("Location").split("/")[1]).isEqualTo("reservation-waitings");
        Assertions.assertThat(reservationWaiting1.header("Location").split("/")[2]).isEqualTo("1"); // init 1
        createReservationWaiting(token3); // 2

        // when delete 1
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservation-waitings/" + reservationWaiting1.header("Location").split("/")[2])
                .then().log().all()
                .extract();

        Member member4 = saveMember(jdbcTemplate, "USER4", "PASS1", "ADMIN");
        String token4 = jwtTokenProvider.createToken(String.valueOf(member4.getId()), member4.getRole());
        ExtractableResponse<Response> reservationWaiting3 = createReservationWaiting(token4);
        Assertions.assertThat(reservationWaiting3.header("Location").split("/")[2]).isEqualTo("3"); // max + 1
    }

    private ExtractableResponse<Response> createReservationWaiting(String accessToken){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
    }
}