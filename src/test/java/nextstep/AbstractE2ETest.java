package nextstep;

import auth.domain.dto.TokenRequest;
import auth.domain.dto.TokenResponse;
import io.restassured.RestAssured;
import nextstep.domain.dto.request.MemberRequest;
import nextstep.domain.dto.request.ScheduleRequest;
import nextstep.domain.dto.request.ThemeRequest;
import nextstep.util.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static nextstep.util.RequestBuilder.*;
import static nextstep.util.RequestBuilder.memberRequestForAdmin;
import static nextstep.util.RequestBuilder.tokenRequestForAdmin;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractE2ETest {
    @Autowired
    private DatabaseCleaner databaseCleaner;
    protected TokenResponse token;

    @BeforeEach
    protected void setUp() {
        databaseCleaner.execute();
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequestForAdmin())
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequestForAdmin())
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        token = response.as(TokenResponse.class);

    }

    public Long createTheme() {
        var themeResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(themeRequest())
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");
        return Long.parseLong(themeLocation[themeLocation.length - 1]);
    }

    public Long createSchedule(Long themeId) {
        var scheduleResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(scheduleRequest(themeId))
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        return Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);
    }

    public void createUser() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequestForUser())
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    public Long createReservation(Long scheduleId) {
        var reservationResponse = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(RequestBuilder.reservationRequest(scheduleId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
        String[] reservationLocation = reservationResponse.header("Location").split("/");
        return Long.parseLong(reservationLocation[reservationLocation.length - 1]);
    }

    public String tokenForUser() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequestForUser())
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(TokenResponse.class)
                .getAccessToken();
    }
}
