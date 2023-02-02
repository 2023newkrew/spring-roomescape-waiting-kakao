package roomwaiting.auth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomwaiting.nextstep.RoomEscapeApplication;
import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.member.MemberRequest;
import roomwaiting.nextstep.reservation.domain.Reservation;
import roomwaiting.nextstep.reservation.dto.ReservationRequest;
import roomwaiting.nextstep.schedule.ScheduleRequest;
import roomwaiting.nextstep.theme.ThemeRequest;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomwaiting.nextstep.AbstractE2ETest.*;
import static roomwaiting.nextstep.reservation.ReservationTest.TEST_DATE;
import static roomwaiting.nextstep.reservation.ReservationTest.TEST_TIME;

@DisplayName("Admin 권한 테스트")
@SpringBootTest(classes = {RoomEscapeApplication.class})
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class AdminAuthorizationTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    private Long themeId;
    private Long scheduleId;
    private String token;

    @BeforeEach
    void setUp() {
        Member member = saveMember(jdbcTemplate, USERNAME, PASSWORD, "MEMBER");
        ExtractableResponse<Response> tokenResponse = generateToken(member.getUsername(), member.getPassword());
        token = tokenResponse.body().jsonPath().getString("accessToken");

        Member admin = saveMember(jdbcTemplate, USERNAME + "admin", PASSWORD, "ADMIN");
        ExtractableResponse<Response> tokenAdminResponse = generateToken(admin.getUsername(), admin.getPassword());
        String adminToken = tokenAdminResponse.body().jsonPath().getString("accessToken");
        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 10000);
        var themeResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken)
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
                .auth().oauth2(adminToken)
                .body(scheduleRequest)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);
    }

    @DisplayName("일반 멤버는 예약을 등록할 수 있다")
    @Test
    void registerReservationMemberTest() {
        ExtractableResponse<Response> reservation = requestCreateReservation();
        Assertions.assertThat(reservation.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("일반 멤버는 자신의 예약을 조회할 수 있다")
    @Test
    void lookUpReservationMemberTest() {
        requestCreateReservation();
        var reservation = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", TEST_DATE)
                .auth().oauth2(token)
                .when().get("/reservations")
                .then().log().all()
                .extract();
        List<Reservation> reservations = reservation.jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("일반 멤버는 자신의 예약을 삭제할 수 있다")
    @Test
    void deleteReservationMemberTest() {
        ExtractableResponse<Response> reservation = requestCreateReservation();
        ExtractableResponse<Response> result = RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete(reservation.header("Location"))
                .then().log().all()
                .extract();
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    @DisplayName("일반 멤버는 테마를 등록할 수 없다")
    @Test
    void nonRegisterThemeMemberTest() {
        ExtractableResponse<Response> createTheme = requestCreateTheme();
        Assertions.assertThat(createTheme.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("일반 멤버는 테마를 조회할 수 있다")
    @Test
    void lookUpThemeMemberTest() {
        ExtractableResponse<Response> lookUpTheme = RestAssured
                .given().log().all()
                .param("date", "2022-08-11")
                .auth().oauth2(token)
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        assertThat(lookUpTheme.jsonPath().getList(".").size()).isEqualTo(1);
    }

    @DisplayName("일반 멤버는 테마를 삭제할 수 없다")
    @Test
    void nonDeleteThemeMemberTest() {
        RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete("/admin/themes/" + themeId)
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("일반 멤버는 스케줄을 등록할 수 없다")
    @Test
    void nonRegisterScheduleMemberTest() {
        ExtractableResponse<Response> createSchedule = requestCreateSchedule();
        Assertions.assertThat(createSchedule.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("일반 멤버는 스케줄을 조회할 수 있다")
    @Test
    void lookUpScheduleMemberTest() {
        ExtractableResponse<Response> lookUpSchedule = RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .auth().oauth2(token)
                .param("date", "2022-08-11")
                .when().get("/schedules")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        assertThat(lookUpSchedule.jsonPath().getList(".").size()).isEqualTo(1);
    }

    @DisplayName("일반 멤버는 스케줄을 삭제할 수 없다")
    @Test
    void NonDeleteScheduleMemberTest() {
        RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete("/admin/schedules/" + scheduleId)
                .then().log().all()
                .extract();
    }

    @DisplayName("Admin 유저는 일반 유저를 Admin으로 변경할 수 있다")
    @Test
    void createAdmin(){
        Member memberAdmin = saveMember(jdbcTemplate, "userAdmin", "1234", "ADMIN");
        ExtractableResponse<Response> response = generateToken(memberAdmin.getUsername(), memberAdmin.getPassword());
        String adminToken = response.body().jsonPath().getString("accessToken");

        Member member = saveMember(jdbcTemplate, "userMember", "1234", "MEMBER");
        MemberRequest body = new MemberRequest(member.getUsername(), member.getPassword(), member.getName(), member.getPhone(), member.getRole());

        ExtractableResponse<Response> result = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(adminToken)
                .body(body)
                .when().post("/admin/register/")
                .then().log().all()
                .extract();
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("일반 유저는 다른 일반 유저를 Admin으로 변경할 수 없다")
    @Test
    void nonCreateAdmin(){
        MemberRequest body = new MemberRequest("userMember", "1234", "name", "010-1234-5678", "MEMBER");
        ExtractableResponse<Response> result = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(body)
                .when().post("/admin/register/")
                .then().log().all()
                .extract();
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private ExtractableResponse<Response> requestCreateReservation() {
        ReservationRequest request = new ReservationRequest(
                scheduleId
        );
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestCreateTheme() {
        ThemeRequest body = new ThemeRequest("테마이름111", "테마설명", 22000);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestCreateSchedule() {
        ScheduleRequest body = new ScheduleRequest(themeId, "2022-08-11", "13:00:00");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(body)
                .when().post("/admin/schedules")
                .then().log().all()
                .extract();
    }
}

