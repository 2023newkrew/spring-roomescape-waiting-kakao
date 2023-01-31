package nextstep;

import auth.login.dto.TokenRequest;
import auth.login.dto.TokenResponse;
import io.restassured.RestAssured;
import nextstep.web.member.dto.MemberRequest;
import nextstep.web.reservation.dto.ReservationRequest;
import nextstep.web.reservation_waiting.dto.ReservationWaitingRequest;
import nextstep.web.schedule.dto.ScheduleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractE2ETest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    protected TokenResponse token;

    @BeforeEach
    protected void setUp() {
        MemberRequest memberBody = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "ADMIN");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenBody = new TokenRequest(USERNAME, PASSWORD);
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        token = response.as(TokenResponse.class);
    }

    protected String requestCreateReservationWaiting(ReservationWaitingRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/reservation-waitings")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");
    }

    protected String requestCreateReservation(ReservationRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract().header("Location");
    }

    protected String createAnotherMember() {
        MemberRequest memberBody = new MemberRequest("AnotherUser", "AnotherUser", "user", "010-4321-5678", "USER");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenBody = new TokenRequest("AnotherUser", "AnotherUser");
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        return response.as(TokenResponse.class).getAccessToken();
    }

    protected String requestCreateSchedule(Long themeId) {
        ScheduleRequest body = new ScheduleRequest(themeId, "2022-08-11", "13:00");
        return RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .header("Location");
    }
}
