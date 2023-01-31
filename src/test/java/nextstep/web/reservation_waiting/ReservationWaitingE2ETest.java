package nextstep.web.reservation_waiting;

import io.restassured.RestAssured;
import nextstep.AbstractE2ETest;
import nextstep.web.reservation.dto.ReservationRequest;
import nextstep.web.reservation_waiting.dto.ReservationWaitingRequest;
import nextstep.web.reservation_waiting.dto.ReservationWaitingResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationWaitingE2ETest extends AbstractE2ETest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void cleanUpTable() {
        jdbcTemplate.update("TRUNCATE TABLE reservation_waiting;");
    }

    @Test
    @DisplayName("예약 대기를 생성한다. (해당 스케줄에 예약이 없을 경우)")
    void createReservationWaitingNoReservation() {
        ReservationWaitingRequest request = new ReservationWaitingRequest(1L);
        String location = requestCreateReservationWaiting(request);

        assertThat(location.startsWith("/reservations/")).isTrue();
    }

    @Test
    @DisplayName("예약 대기를 생성한다. (해당 스케줄에 예약이 이미 있을 경우)")
    void createReservationWaitingExistReservation() {
        createReservation(1L);

        ReservationWaitingRequest request = new ReservationWaitingRequest(1L);
        var location = requestCreateReservationWaiting(request);

        assertThat(location.startsWith("/reservation-waitings/")).isTrue();
    }

    private void createReservation(Long scheduleId) {
        ReservationRequest request = new ReservationRequest(
                scheduleId
        );
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    @Test
    @DisplayName("자신의 예약 대기를 조회할 수 있다.")
    void findMyReservationWaitings() {
        createReservation(1L);
        createReservation(2L);
        requestCreateReservationWaiting(new ReservationWaitingRequest(1L));
        requestCreateReservationWaiting(new ReservationWaitingRequest(2L));

        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/reservation-waitings/mine")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<ReservationWaitingResponse> responses = response.jsonPath().getList(".", ReservationWaitingResponse.class);
        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("자신의 예약 대기를 취소할 수 있다.")
    void deleteMyReservationWaiting() {
        createReservation(1L);
        String location = requestCreateReservationWaiting(new ReservationWaitingRequest(1L));
        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    @Test
    @DisplayName("자신의 예약 대기가 아니면 취소할 수 없다.")
    void deleteOtherReservationWaiting() {
        createReservation(1L);
        String location = requestCreateReservationWaiting(new ReservationWaitingRequest(1L));

        String anotherToken = createAnotherMember();

        RestAssured
                .given().log().all()
                .auth().oauth2(anotherToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .extract();
    }

    @Test
    @DisplayName("존재하지 않는 예약대기를 취소할 수 없다.")
    void deleteNotExistReservationWaiting() {
        String location = "/reservation-waitings/1";

        RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract();
    }
}
