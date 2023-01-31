package nextstep.reservation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
import nextstep.DatabaseCleaner;
import nextstep.domain.dto.request.ReservationRequest;
import nextstep.domain.dto.response.ReservationResponse;
import nextstep.domain.enumeration.ReservationStatus;
import nextstep.repository.ProfitDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ReservationWithProfitE2ETest extends AbstractE2ETest {
    @Autowired
    private ProfitDao profitDao;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private DatabaseCleaner databaseCleaner;
    private ReservationRequest request;
    private Long themeId;
    private Long scheduleId;

    @BeforeEach
    public void setUp() {
        databaseCleaner.execute();
        super.setUp();

        themeId = super.createTheme();
        scheduleId = super.createSchedule(themeId);
        request = new ReservationRequest(scheduleId);
    }

    @Test
    @DisplayName("예약을 승인하면 예약금 내역이 추가된다.")
    void Should_InsertProfit_When_IfApprovedReservation() throws InterruptedException {
        createReservation();

        given().
                auth().oauth2(token.getAccessToken()).
                when().
                patch("/reservations/1/approve").
                then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        threadPoolTaskExecutor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);
        assertThat(profitDao.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("미승인 상태의 예약을 거절할 경우 환불 없이 예약이 거절된다.")
    void Should_NotInsertProfitAndRejectReservation_When_IfRejectReservationIsNotApproved() throws InterruptedException {
        createReservation();

        given().
                auth().oauth2(token.getAccessToken()).
                when().
                patch("/reservations/1/reject").
                then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        threadPoolTaskExecutor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);
        assertThat(profitDao.findAll().size()).isEqualTo(0);

        var response = given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservationResponses = response.jsonPath().getList(".", ReservationResponse.class);
        assertThat(reservationResponses.get(0).getStatus()).isEqualTo(ReservationStatus.REJECTED);
    }

    @Test
    @DisplayName("승인 상태의 예약을 거절할 경우 환불 이력이 추가되고 예약이 거절된다.")
    void Should_InsertProfitAndRejectReservation_When_IfRejectReservationIsApproved() throws InterruptedException {
        createReservation();

        given().
                auth().oauth2(token.getAccessToken()).
        when().
                patch("/reservations/1/approve").
        then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        given().
                auth().oauth2(token.getAccessToken()).
        when().
                patch("/reservations/1/reject").
        then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        threadPoolTaskExecutor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);
        assertThat(profitDao.findAll().size()).isEqualTo(2);

        var response = given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservationResponses = response.jsonPath().getList(".", ReservationResponse.class);
        assertThat(reservationResponses.get(0).getStatus()).isEqualTo(ReservationStatus.REJECTED);
    }

    @Test
    @DisplayName("예약 취소 대기 상태의 예약을 취소하면 환불 이력이 추가되고 예약이 취소된다.")
    void Should_InsertProfitAndCancelReservation_When_IfCancelReservationIsWaited() throws InterruptedException {
        createReservation();

        given().
                auth().oauth2(token.getAccessToken()).
                when().
                patch("/reservations/1/approve").
                then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        given().
                auth().oauth2(token.getAccessToken()).
                when().
                patch("/reservations/1/cancel").
                then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        given().
                auth().oauth2(token.getAccessToken()).
                when().
                get("/reservations/1/cancel-approve").
                then().
                assertThat().
                statusCode(HttpStatus.OK.value());

        threadPoolTaskExecutor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);
        assertThat(profitDao.findAll().size()).isEqualTo(2);

        var response = given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/reservations/mine")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservationResponses = response.jsonPath().getList(".", ReservationResponse.class);
        assertThat(reservationResponses.get(0).getStatus()).isEqualTo(ReservationStatus.CANCELED);
    }

    private ExtractableResponse<Response> createReservation() {
        return given().log().all()
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/reservations")
                .then().log().all()
                .extract();
    }
}
