package nextstep.reservationwaiting;

import auth.dto.request.TokenRequest;
import auth.dto.response.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.dto.request.MemberRequest;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.request.ScheduleRequest;
import nextstep.dto.request.ThemeRequest;
import nextstep.dto.response.ReservationWaitingResponse;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ReservationWaitingE2ETest extends AbstractE2ETest {

    public static final String DATE = "2022-08-11";
    public static final String TIME = "13:00";

    private ReservationRequest request;
    private Long themeId;
    private Long scheduleId;

    @BeforeEach
    public void setUp() {
        super.setUp();
        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
        var themeResponse = given().log().all()
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
        var scheduleResponse = given().log().all()
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
    }

    @Test
    void 본인의_예약_대기를_취소할_수_있다() {
        // given
        createReservation(token.getAccessToken());
        ExtractableResponse<Response> reservationWaitingResponse = createReservation(token.getAccessToken());

        given()
                .auth().oauth2(token.getAccessToken())

        // when
        .when()
                .delete(reservationWaitingResponse.header(HttpHeaders.LOCATION))

        // then
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 본인의_예약_대기가_아닌_경우_취소할_수_없다() {
        // given
        createReservation(token.getAccessToken());
        ExtractableResponse<Response> reservationWaitingResponse = createReservation(token.getAccessToken());
        TokenResponse anotherToken = createAnotherToken();

        given()
                .auth().oauth2(anotherToken.getAccessToken())

        // when
        .when()
                .delete(reservationWaitingResponse.header(HttpHeaders.LOCATION))

        // then
        .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 예약_대기를_조회할_경우_대기_순번호_함께_조회할_수_있다() {
        // given
        TokenResponse anotherToken = createAnotherToken();
        createReservation(token.getAccessToken());
        createReservation(anotherToken.getAccessToken());
        createReservation(token.getAccessToken());

        List<ReservationWaitingResponse> response = given()
                .auth().oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)

        // when
        .when()
                .get("/reservation-waitings/mine")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<List<ReservationWaitingResponse>>() {});

        assertThat(response.get(0).getWaitNum()).isEqualTo(2);
    }

    private ExtractableResponse<Response> createReservation(String token) {
        return given().log().all()
                .auth().oauth2(token)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }

    private TokenResponse createAnotherToken() {
        createAnotherMember();
        TokenRequest tokenBody = new TokenRequest("eddie", "kk");
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        return response.as(TokenResponse.class);
    }

    private void createAnotherMember() {
        MemberRequest memberBody = new MemberRequest("eddie", "kk", "name", "010-1234-5678", "ADMIN");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

}
