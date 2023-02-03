package nextstep.reservationwaiting;

import auth.TokenRequest;
import auth.TokenResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.AbstractE2ETest;
import nextstep.member.MemberRequest;
import nextstep.reservation.Reservation;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationWaitingControllerTest extends AbstractE2ETest {
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";
    private static final String DEFAULT_PATH = "/reservation-waitings";

    private Long themeId;
    private ReservationWaitingRequest waitingRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
        Response response = post(givenWithAuth(), "/admin/themes", themeRequest);
        var themeResponse = then(response)
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);
        response = post(givenWithAuth(), "/admin/schedules", scheduleRequest);
        var scheduleResponse = then(response)
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        Long scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);

        waitingRequest = new ReservationWaitingRequest(
                scheduleId
        );
    }

    @DisplayName("예약이 되어있는 스케줄에 예약 대기를 신청한다.")
    @Test
    void create() {
        createReservation();
        Response response = post(givenWithAuth(), DEFAULT_PATH, waitingRequest);

        then(response)
                .header("Location", "/reservation-waitings/1")
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("예약이 안된 스케줄에 예약 대기를 신청하면 예약이 되어야 한다.")
    @Test
    void create_reservation() {
        Response response = post(givenWithAuth(), DEFAULT_PATH, waitingRequest);

        then(response)
                .header("Location", "/reservations/1")
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("비로그인 사용자가 예약 대기를 신청하면 예외처리 되어야 한다.")
    @Test
    void createWithoutLogin() {
        Response response = post(given(), DEFAULT_PATH, waitingRequest);

        then(response)
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("같은 스케쥴에 예약 대기를 신청하면 waitNum 이 증가해야 한다.")
    @Test
    void waitNumTest() {
        createReservation();
        createReservationWaiting();

        Response response = get(givenWithAuth(), DEFAULT_PATH + "/mine");
        long before = then(response)
                .extract()
                .jsonPath().getLong("[0].waitNum");

        createReservationWaiting();

        response = get(givenWithAuth(), DEFAULT_PATH + "/mine");
        long after = then(response)
                .extract()
                .jsonPath().getLong("[1].waitNum");

        assertThat(after).isGreaterThan(before);
    }

    @DisplayName("자신의 예약 대기 목록을 조회할 수 있다.")
    @Test
    void show() {
        createReservation();
        createReservationWaiting();

        Response response = get(givenWithAuth(), DEFAULT_PATH + "/mine");

        List<ReservationWaitingResponse> reservationWaitings = then(response).extract()
                .jsonPath().getList(".", ReservationWaitingResponse.class);
        assertThat(reservationWaitings.size()).isEqualTo(1);
    }

    @DisplayName("예약 대기가 삭제되어야 한다.")
    @Test
    void delete() {
        createReservation();
        var reservationWaiting = createReservationWaiting();

        Response response = delete(givenWithAuth(), reservationWaiting.header("Location"));

        then(response)
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 예약대기를 삭제하려는 경우 예외처리 되어야 한다.")
    @Test
    void deleteNotExistReservationWaiting() {
        createReservation();

        Response response = delete(givenWithAuth(), DEFAULT_PATH + "/1");
        then(response)
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("다른 사용자가 등록한 예약 대기는 취소할 수 없어야 한다.")
    @Test
    void deleteWaitingOfOther() {
        TokenResponse othersToken = createOtherMemberToken();

        createReservation();
        var othersResponseOfPost = post(given().auth().oauth2(othersToken.getAccessToken()), DEFAULT_PATH, waitingRequest);
        var othersReservationWaiting = then(othersResponseOfPost).extract();

        Response response = delete(givenWithAuth(), othersReservationWaiting.header("Location"));

        then(response)
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("예약을 삭제하면 가장 빠른 예약 대기가 예약되어야 한다.")
    @Test
    void deleteReservation() {
        var reservation = createReservation();
        createReservationWaiting();

        var deleteResponse = delete(givenWithAuth(), reservation.header("Location"));
        then(deleteResponse)
                .statusCode(HttpStatus.NO_CONTENT.value());

        RequestSpecification given = given()
                .param("themeId", Long.toString(themeId))
                .param("date", DATE);
        var reservationResponse = get(given, "/reservations");
        List<Reservation> reservations = reservationResponse.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(1);

        var reservationWaitingResponse = get(givenWithAuth(), DEFAULT_PATH + "/mine");
        List<ReservationWaiting> reservationWaitings = reservationWaitingResponse.jsonPath().getList(".", ReservationWaiting.class);
        assertThat(reservationWaitings.size()).isEqualTo(0);
    }

    private TokenResponse createOtherMemberToken() {
        MemberRequest memberBody = new MemberRequest("other", "9999", "other", "010-1111-2222", "USER");
        then(post(given(), "/members", memberBody))
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenBody = new TokenRequest("other", "9999");
        return then(post(given(), "/login/token", tokenBody))
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(TokenResponse.class);
    }

    private ExtractableResponse<Response> createReservationWaiting() {
        Response response = post(givenWithAuth(), DEFAULT_PATH, waitingRequest);
        return then(response)
                .extract();
    }

    private ExtractableResponse<Response> createReservation() {
        Response response = post(givenWithAuth(), "/reservations", waitingRequest);
        return then(response)
                .extract();
    }
}