package nextstep.waiting;

import io.restassured.RestAssured;
import nextstep.AbstractE2ETest;
import nextstep.domain.dto.request.ReservationRequest;
import nextstep.domain.dto.response.ReservationResponse;
import nextstep.domain.dto.response.WaitingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitingE2ETest extends AbstractE2ETest {
    private ReservationRequest request;
    private Long themeId;
    private Long scheduleId;

    @BeforeEach
    public void setUp() {
        super.setUp();

        themeId = super.createTheme();
        scheduleId = super.createSchedule(themeId);
        request = new ReservationRequest(scheduleId);
    }


    @DisplayName("예약 대기를 생성한다")
    @Test
    void Should_CreateWaiting_When_Request() {
        createReservation(scheduleId);

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("예약 대기를 삭제한다")
    @Test
    void Should_DeleteWaiting_When_Request() {
        createReservation(scheduleId);
        Long reservationWaitingId = createReservationWaiting();
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().delete("/reservation-waitings/" + reservationWaitingId)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("예약이 없을 때 예약 요청 후 예약 대기를 조회한다")
    @Test
    void Should_NotCreateWaiting_When_IfCreateWaitingNoReservationExists() {
        createReservationWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservationResponses = response.jsonPath().getList(".", ReservationResponse.class);
        assertThat(reservationResponses.size()).isEqualTo(0);
    }

    @DisplayName("예약이 있을 때 예약 요청 후 예약 대기를 조회한다")
    @Test
    void Should_CreateWaiting_When_IfCreateWaitingReservationExists() {
        createReservationWaiting();
        createReservationWaiting();

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .extract();

        List<WaitingResponse> reservationResponses = response.jsonPath().getList(".", WaitingResponse.class);
        assertThat(reservationResponses.size()).isEqualTo(1);
    }

    private Long createReservationWaiting() {
        return createReservation(scheduleId);
    }
}

