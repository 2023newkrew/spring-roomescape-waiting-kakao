package roomwaiting.nextstep.reservation;

import org.assertj.core.api.Assertions;
import roomwaiting.auth.token.JwtTokenProvider;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomwaiting.nextstep.RoomEscapeApplication;
import roomwaiting.AcceptanceTestExecutionListener;
import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.domain.Reservation;
import roomwaiting.nextstep.reservation.dto.ReservationRequest;
import roomwaiting.nextstep.schedule.ScheduleRequest;
import roomwaiting.nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import roomwaiting.nextstep.AbstractE2ETest;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = {RoomEscapeApplication.class})
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class ReservationTest extends AbstractE2ETest {
    public static final String TEST_DATE = "2022-08-11";
    public static final String TEST_TIME = "13:00";

    private ReservationRequest request;
    private Long themeId;
    private Long scheduleId;

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    Environment env;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
        var themeResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .body(themeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, TEST_DATE, TEST_TIME);
        var scheduleResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
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
    }

    @DisplayName("허용되지 않은 사용자가 예약을 이용할 때, 에러가 발생한다")
    @Test
    void notAuthorizedUserTest(){
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("스케줄이 있는 경우, 예약을 생성할 수 있다.")
    @Test
    void create() {
        RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("스케줄이 없는 경우, 예약을 생성하면 에러가 발생한다.")
    @Test
    void createError() {
        request = new ReservationRequest(
                scheduleId+2
        );
        RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("중복 예약을 생성할 경우, 에러가 발생한다")
    @Test
    void createDuplicateReservation() {
        requestCreateReservation();
        ExtractableResponse<Response> createReservation = requestCreateReservation();
        assertThat(createReservation.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }


    @DisplayName("예약을 조회할 수 있다")
    @Test
    void show() {
        requestCreateReservation();

        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", TEST_DATE)
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("예약이 없을 때 예약 목록은 비어있다.")
    @Test
    void showEmptyReservations() {
        var response = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", TEST_DATE)
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations")
                .then().log().all()
                .extract();

        List<Reservation> reservations = response.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(0);
    }

    @DisplayName("예약을 삭제할 수 있다")
    @Test
    void delete() {
        var reservation = requestCreateReservation();
        ExtractableResponse<Response> result = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .extract();
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("없는 예약을 삭제할 경우, 에러가 발생한다")
    @Test
    void createNotExistReservation() {
        RestAssured
            .given().log().all()
            .auth().oauth2(token.getAccessToken())
            .when().delete("/reservations/22221")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("다른 회원이 삭제하는 경우, 에러가 발생한다")
    @Test
    void otherUserDeleteTest(){
        var reservation = requestCreateReservation();
        String otherUserName = AbstractE2ETest.USERNAME + "22";
        Member member = AbstractE2ETest.saveMember(jdbcTemplate, otherUserName, AbstractE2ETest.PASSWORD, AbstractE2ETest.ADMIN);
        ExtractableResponse<Response> otherTokenResponse = AbstractE2ETest.generateToken(member.getUsername(), member.getPassword());
        String otherToken = otherTokenResponse.body().jsonPath().getString("accessToken");

        RestAssured
                .given().log().all()
                .auth().oauth2(otherToken)
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> requestCreateReservation() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }
}
