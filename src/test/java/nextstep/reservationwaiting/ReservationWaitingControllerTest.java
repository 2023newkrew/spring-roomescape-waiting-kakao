package nextstep.reservationwaiting;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.AbstractE2ETest;
import nextstep.reservationwaiting.dto.ReservationWaitingRequest;
import nextstep.reservationwaiting.dto.ReservationWaitingResponse;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ReservationWaitingControllerTest extends AbstractE2ETest {

    @Autowired
    public ReservationWaitingController reservationWaitingController;

    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";
    private static final String DEFAULT_PATH = "/reservation-waitings";

    private ReservationWaitingRequest waitingRequest;
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

        waitingRequest = new ReservationWaitingRequest(
                scheduleId
        );
    }

    @DisplayName("예약이 된 스케줄에 예약 대기를 신청한다")
    @Test
    void create() {
        createReservation();
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(DEFAULT_PATH)
                .then().log().all()
                .header("Location", "/reservation-waitings/1")
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("예약이 안된 스케줄에 예약 대기를 신청하면 예약이 된다.")
    @Test
    void create_reservation() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(DEFAULT_PATH)
                .then().log().all()
                .header("Location", "/reservations/1")
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("비로그인 사용자가 예약 대기를 생성하면 에러가 발생해야 한다.")
    @Test
    void createWithoutLogin() {
        createReservation();
        var response = RestAssured
                .given().log().all()
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(DEFAULT_PATH)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("같은 스케쥴에 예약하면 waitNum이 증가한다.")
    @Test
    void waitNumTest() {
        createReservation();
        createReservationWaiting();

        long before = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(DEFAULT_PATH + "/mine")
                .then().log().all()
                .extract()
                .jsonPath().getLong("[0].waitNum");

        createReservationWaiting();

        long after = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(DEFAULT_PATH + "/mine")
                .then().log().all()
                .extract()
                .jsonPath().getLong("[1].waitNum");

        assertThat(before).isLessThan(after);
    }

    @DisplayName("예약 대기 목록을 조회한다")
    @Test
    void show() {
        createReservation();
        createReservationWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get(DEFAULT_PATH + "/mine")
                .then().log().all()
                .extract();

        List<ReservationWaitingResponse> reservationWaitings = response.jsonPath().getList(".", ReservationWaitingResponse.class);
        assertThat(reservationWaitings.size()).isEqualTo(1);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void delete() {
        createReservation();
        var reservationWaiting = createReservationWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(reservationWaiting.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("없는 예약대기를 삭제한다")
    @Test
    void deleteNotExistReservationWaiting() {
        createReservation();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(DEFAULT_PATH + "/1")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private ExtractableResponse<Response> createReservationWaiting() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(DEFAULT_PATH)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> createReservation() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }
}