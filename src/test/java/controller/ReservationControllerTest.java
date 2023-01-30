package controller;

import auth.domain.TokenData;
import io.restassured.specification.RequestSpecification;
import nextstep.member.dto.MemberRequest;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.schedule.dto.ScheduleRequest;
import nextstep.theme.dto.ThemeRequest;
import nextstep.waiting.dto.WaitingRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

public class ReservationControllerTest extends AbstractControllerTest {

    static final String DEFAULT_PATH = "/reservations";

    @BeforeEach
    void setUp() {
        createMember();
        createTheme();
        createSchedule();
        createReservation();
        createWaiting();
    }

    private void createTheme() {
        var request = new ThemeRequest("theme", "theme", 10000);
        post(authGiven(), "/admin/themes", request);
    }

    private void createMember() {
        var request1 = new MemberRequest("member", "password", "member", "-");
        post(given(), "/members", request1);
        var request2 = new MemberRequest("member2", "password", "member", "-");
        post(given(), "/members", request2);
    }

    private void createSchedule() {
        var request = new ScheduleRequest("2021-01-01", "00:00", 1L);
        post(given(), "/schedules", request);
    }

    private void createReservation() {
        var request = new ReservationRequest(1L);
        post(authGiven(), "/reservations", request);
    }

    private void createWaiting() {
        var request = new WaitingRequest(1L);
        post(authGivenAnother(), "/reservation-waitings", request);
    }

    private RequestSpecification authGivenAnother() {
        String anotherToken = provider.createToken(new TokenData(2L, "ADMIN"));

        return given()
                .auth()
                .oauth2(anotherToken);
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class delete {
        @DisplayName("예약 취소 시 예약 대기가 예약으로 바뀌는지 확인")
        @Test
        void should_deleteWaiting_when_mine() {
            delete(authGiven(), DEFAULT_PATH + "/1");

            var response = get(authGivenAnother(), DEFAULT_PATH + "/mine");

            then(response)
                    .body("member.id", Matchers.contains(2));
        }
    }
}
