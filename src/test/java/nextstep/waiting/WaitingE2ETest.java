package nextstep.waiting;

import auth.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import nextstep.AbstractE2ETest;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static nextstep.member.MemberE2ETest.createNormalMemberAndIssueToken;
import static nextstep.reservation.ReservationE2ETest.createReservation;
import static nextstep.schedule.ScheduleE2ETest.createSchedule;
import static nextstep.theme.ThemeE2ETest.createTheme;
import static org.assertj.core.api.Assertions.assertThat;

public class WaitingE2ETest extends AbstractE2ETest {

    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private TokenResponse normalMember;
    private Long themeId;
    private Long scheduleId;

    @BeforeEach
    public void setUp() {
        super.setUp();
        normalMember = createNormalMemberAndIssueToken();

        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
        themeId = createTheme(themeRequest);

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);
        scheduleId = createSchedule(scheduleRequest);

        ReservationRequest reservationRequest = new ReservationRequest(scheduleId);
        createReservation(normalMember.getAccessToken(), reservationRequest);
    }

    @DisplayName("예약 대기를 생성한다")
    @Test
    void createWaiting() {
        TokenResponse newNormalMember = createNormalMemberAndIssueToken();
        WaitingRequest waitingRequest = new WaitingRequest(scheduleId);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(newNormalMember.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
        String[] locationInformation = response.header("Location").split("/");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(locationInformation[1]).isEqualTo("reservation-waitings");
    }

    @DisplayName("예약이 없는 스케줄에 대해서는 예약 대기가 아닌 예약이 생성된다")
    @Test
    void createWaitingToReservation() {
        String differentDate = "2022-09-10";
        String differentTime = "16:00";
        Long scheduleId = createSchedule(new ScheduleRequest(themeId, differentDate, differentTime));

        WaitingRequest waitingRequest = new WaitingRequest(scheduleId);

        TokenResponse newNormalMember = createNormalMemberAndIssueToken();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(newNormalMember.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
        String[] locationInformation = response.header("Location").split("/");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(locationInformation[1]).isEqualTo("reservations");
    }

    @DisplayName("비로그인 사용자는 예약 대기를 할 수 없다")
    @Test
    void createWaitingWithoutLogin() {
        WaitingRequest waitingRequest = new WaitingRequest(scheduleId);

        var response = RestAssured
                .given().log().all()
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("없는 스케줄 ID에 대해서는 예약대기를 할 수 없다.")
    @Test
    void createWaitingWithUnavailableId() {
        TokenResponse newNormalMember = createNormalMemberAndIssueToken();
        WaitingRequest waitingRequest = new WaitingRequest(Long.MAX_VALUE);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(newNormalMember.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("사용자가 예약 대기한 것에 대해 정보를 받을 수 있다.")
    @Test
    void getWaiting() {
        Long otherScheduleId = createSchedule(new ScheduleRequest(themeId, "2022-08-12", "15:00"));
        createReservation(normalMember.getAccessToken(), new ReservationRequest(otherScheduleId));

        TokenResponse newNormalMember = createNormalMemberAndIssueToken();
        createWaiting(newNormalMember.getAccessToken(), new WaitingRequest(scheduleId));

        TokenResponse otherNormalMember = createNormalMemberAndIssueToken();
        createWaiting(otherNormalMember.getAccessToken(), new WaitingRequest(scheduleId));
        createWaiting(otherNormalMember.getAccessToken(), new WaitingRequest(otherScheduleId));

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(otherNormalMember.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("사용자가 등록한 예약 대기를 삭제할 수 있다")
    @Test
    void deleteWaiting() {
        TokenResponse newNormalMember = createNormalMemberAndIssueToken();
        WaitingRequest waitingRequest = new WaitingRequest(scheduleId);
        Long waitingId = createWaiting(newNormalMember.getAccessToken(), waitingRequest);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(newNormalMember.getAccessToken())
                .when().delete("/reservation-waitings/" + waitingId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("등록되지 않은 예약 대기가 아니라면 삭제할 수 없다.")
    @Test
    void deleteOnlyRegistered() {
        TokenResponse newNormalMember = createNormalMemberAndIssueToken();
        WaitingRequest waitingRequest = new WaitingRequest(scheduleId);
        Long waitingId = createWaiting(newNormalMember.getAccessToken(), waitingRequest);
        Long invalidWaitingId = waitingId + 1L;

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(newNormalMember.getAccessToken())
                .when().delete("/reservation-waitings/" + invalidWaitingId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("해당 사용자의 예약대기가 아니라면 삭제할 수 없다.")
    @Test
    void deleteOnlyMyWaiting() {
        TokenResponse newNormalMember = createNormalMemberAndIssueToken();
        WaitingRequest waitingRequest = new WaitingRequest(scheduleId);
        Long waitingId = createWaiting(newNormalMember.getAccessToken(), waitingRequest);

        TokenResponse otherNormalMember = createNormalMemberAndIssueToken();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(otherNormalMember.getAccessToken())
                .when().delete("/reservation-waitings/" + waitingId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static Long createWaiting(String token, WaitingRequest waitingRequest) {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();
        String[] waitingLocation = response.header("Location").split("/");
        return Long.parseLong(waitingLocation[waitingLocation.length - 1]);
    }
}
