package nextstep.waiting;

import auth.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.reservationwaiting.ReservationWaitingRequest;
import nextstep.reservationwaiting.ReservationWaitingResponse;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationWaitingE2ETest extends AbstractE2ETest {

    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private ReservationWaitingRequest request;
    private Long themeId;
    private Long scheduleId;

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

        request = new ReservationWaitingRequest(
                scheduleId
        );
    }

    @DisplayName("예약 대기를 생성한다")
    @Test
    void create() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/reservation-waitings/1");
    }


    @DisplayName("내 예약 대기를 취소한다")
    @Test
    void cancel() {
        var reservation = createReservationWaiting(token);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().put(reservation.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("다른 사람이 예약 대기를 취소한다")
    @Test
    void deleteReservationOfOthers() {
        createReservationWaiting(token);
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(someoneToken.getAccessToken())
                .when().put("/reservation-waitings/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("내 예약 대기를 조회한다 - 처음 예약 대기 신청은 예약으로 전환, 나머지는 예약 대기 상태")
    @Test
    void findMine() {
        createReservationWaiting(token);
        createReservationWaiting(token);
        createReservationWaiting(someoneToken);
        createReservationWaiting(someoneToken);
        createReservationWaiting(someoneToken);
        createReservationWaiting(token);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<ReservationWaitingResponse> reservationWaitingResponseList = response.jsonPath().getList("data", ReservationWaitingResponse.class);
        assertThat(reservationWaitingResponseList.size()).isEqualTo(2);
        assertThat(reservationWaitingResponseList.get(0).getWaitNum()).isEqualTo(1);
        assertThat(reservationWaitingResponseList.get(1).getWaitNum()).isEqualTo(5);

    }

    private ExtractableResponse<Response> createReservationWaiting(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }


}
