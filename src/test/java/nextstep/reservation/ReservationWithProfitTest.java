package nextstep.reservation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AbstractE2ETest;
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
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

public class ReservationWithProfitTest extends AbstractE2ETest {
    @Autowired
    private ProfitDao profitDao;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
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
