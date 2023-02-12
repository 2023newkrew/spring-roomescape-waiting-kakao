package nextstep.waitings;

import auth.TokenRequest;
import auth.TokenResponse;
import io.restassured.RestAssured;
import nextstep.AbstractE2ETest;
import nextstep.member.MemberRequest;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitingE2ETest extends AbstractE2ETest {
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";
    public static final String GUEST_ID = "guest_id";
    public static final String GUEST_PASSWORD = "guest_password";

    private ReservationRequest request;

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
        Long themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);

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

    @DisplayName("이미 예약이 된 스케줄 대상으로 예약 대기를 신청할 수 있다")
    @Test
    void createWaitingWhenReserved() {
        RestAssured.given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .header("Location", "/reservation-waitings/1")
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("예약이 없는 스케줄에 대해서 예약 대기 신청을 할 경우 예약이 된다")
    @Test
    void createReservationWhenNotReserved(){
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .header("Location", "/reservations/1")
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("자신의 예약 대기를 취소할 수 있다")
    @Test
    void deleteMyReservation() {
        createReservationAndWaiting(adminToken, request);

        RestAssured.given().log().all() // delete reservation waiting
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("자신의 존재하지 않는 예약 대기를 취소할 수 없다")
    @Test
    void cannotDeleteMyNotExistReservation() {
        RestAssured.given().log().all() // delete reservation waiting
                .auth().oauth2(adminToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("자신의 예약 대기가 아닌 경우 취소할 수 없다.")
    @Test
    void cannotDeleteOtherReservation() {
        createReservationAndWaiting(adminToken, request);

        TokenResponse otherToken = createMemberAndToken("otherUser", "otherPassword");

        RestAssured.given().log().all() // delete reservation waiting with other auth
                .auth().oauth2(otherToken.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/reservation-waitings/1")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("나의 예약 대기 목록을 조회할 수 있다")
    @Test
    void showMyWaitings() {
        createReservationAndWaiting(adminToken, request);

        var response = RestAssured.given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(1);
    }

    @DisplayName("예약 대기 목록이 두 개 이상인 경우 waitNum이 정상적으로 출력된다")
    @Test
    void showSameScheduleWaitings() {
        createReservationAndWaiting(adminToken, request);
        TokenResponse otherTokenResponse = createMemberAndToken("guest1", "guest1");
        createWaiting(otherTokenResponse,request);

        var response = RestAssured.given().log().all()
                .auth().oauth2(otherTokenResponse.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(1);
        assertThat(response.jsonPath().getLong("[0].waitNum")).isEqualTo(2L);
    }

    @DisplayName("스케줄이 다른 경우 waitNum이 각 스케줄에 대해 독립적으로 적용된다")
    @Test
    void showDifferentScheduleWaitings() {
        createReservationAndWaiting(adminToken, request);
        TokenResponse otherToken = createMemberAndToken(GUEST_ID, GUEST_PASSWORD);
        ReservationRequest otherRequest = makeAnotherSchedule();
        createReservationAndWaiting(otherToken, otherRequest);

        var response = RestAssured.given().log().all()
                .auth().oauth2(otherToken.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(1);
        assertThat(response.jsonPath().getLong("[0].waitNum")).isEqualTo(1L);
    }

    @DisplayName("나의 예약 대기가 없는 경우 빈 List가 출력된다.")
    @Test
    void showMyEmptyWaitings() {
        var response = RestAssured.given().log().all()
                .auth().oauth2(adminToken.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(0);
    }

    private void createReservationAndWaiting(TokenResponse tokenResponse, ReservationRequest reservationRequest) {
        RestAssured.given().log().all() // resevation create
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(reservationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        createWaiting(tokenResponse, reservationRequest);
    }

    private void createWaiting(TokenResponse token, ReservationRequest reservationRequest) {
        RestAssured.given().log().all() // reservation waiting create
                .auth().oauth2(token.getAccessToken())
                .body(reservationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private TokenResponse createMemberAndToken(String username, String password) {
        MemberRequest otherMemberBody = new MemberRequest(username, password, "name", "010-1234-5678", "USER");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(otherMemberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest otherTokenBody = new TokenRequest(username, password);
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(otherTokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        return response.as(TokenResponse.class);
    }

    private ReservationRequest makeAnotherSchedule() {
        ThemeRequest themeRequest = new ThemeRequest("other_테마이름", "other_테마설명", 22000);
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
        long themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);

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
        long scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);

        return new ReservationRequest(
                scheduleId
        );
    }
}
