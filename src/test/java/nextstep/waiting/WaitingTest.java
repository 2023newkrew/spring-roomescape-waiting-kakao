package nextstep.waiting;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitingTest extends AbstractE2ETest {
    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private TokenResponse token1;
    private TokenResponse token2;

    private ReservationRequest request;
    private WaitingRequest waitingRequest;
    private Long themeId;
    private Long scheduleId;

    @BeforeEach
    void waitingSetUp() {

        MemberRequest memberBody = new MemberRequest("Member1", "PASSWORD", "member1", "010-1234-5678", "USER");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenBody = new TokenRequest("Member1", "PASSWORD");
        var response1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        token1 = response1.as(TokenResponse.class);

        MemberRequest memberBody2 = new MemberRequest("Member2", "PASSWORD", "member2", "010-1234-5678", "USER");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody2)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenBody2 = new TokenRequest("Member2", "PASSWORD");
        var response2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody2)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        token2 = response2.as(TokenResponse.class);

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

        request = new ReservationRequest(
                scheduleId
        );

        waitingRequest = new WaitingRequest(scheduleId);

        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        RestAssured
                .given().log().all()
                .auth().oauth2(token1.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();


    }

    @DisplayName("나의 예약 대기 목록을 확인한다")
    @Test
    void showMyWaiting() {
        var waiting = RestAssured
                .given().log().all()
                .auth().oauth2(token2.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token2.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<WaitingResponse> waitings = response.jsonPath().getList(".", WaitingResponse.class);
        assertThat(waitings.size()).isEqualTo(1);
        assertThat(waitings.get(0).getWaitNum()).isEqualTo(2);
    }

    @DisplayName("예약 대기를 삭제한다")
    @Test
    void deleteWaiting() {
        var waiting = RestAssured
                .given().log().all()
                .auth().oauth2(token2.getAccessToken())
                .body(waitingRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservation-waitings")
                .then().log().all()
                .extract();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token2.getAccessToken())
                .when().delete(waiting.header("Location"))
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
