package controller;

import com.authorizationserver.infrastructures.jwt.TokenData;
import io.restassured.specification.RequestSpecification;
import com.nextstep.interfaces.member.dtos.MemberRequest;
import com.nextstep.interfaces.reservation.dtos.ReservationRequest;
import com.nextstep.interfaces.schedule.dtos.ScheduleRequest;
import com.nextstep.interfaces.theme.dtos.ThemeRequest;
import com.nextstep.interfaces.waiting.dtos.WaitingRequest;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

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
        var request2 = new ScheduleRequest("2021-01-02", "00:00", 1L);
        post(given(), "/schedules", request2);
    }

    private void createReservation() {
        var request = new ReservationRequest(1L);
        post(authGiven(), "/reservations", request);

        var request2 = new ReservationRequest(2L);
        post(authGivenAnother(), "/reservations", request2);
    }

    private void createWaiting() {
        var request = new WaitingRequest(1L);
        post(authGivenAnother(), "/reservation-waitings", request);
    }

    private RequestSpecification authGivenAnother() {
        String anotherToken = provider.createToken(new TokenData(2L, "MEMBER"));

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

            then(response);
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class approve {
        @DisplayName("예약 승인")
        @Test
        void should_approve_reservation() {
            var response = patch(authGiven(), DEFAULT_PATH + "/1/approve");
            then(response)
                    .statusCode(HttpStatus.OK.value());

            response = get(authGiven(), DEFAULT_PATH + "/mine");
            then(response);
        }

        @DisplayName("예약 승인 - 관리자가 아닐 경우")
        @Test
        void should_approve_reservation_not_admin() {
            var response = patch(authGivenAnother(), DEFAULT_PATH + "/1/approve");
            then(response)
                    .statusCode(HttpStatus.FORBIDDEN.value());
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested

    class cancel {
        @DisplayName("예약 취소 - 미승인 상태 - 사용자")
        @Test
        void should_cancel_unapproved_reservation() {
            var response = patch(authGivenAnother(), DEFAULT_PATH + "/2/cancel");
            then(response)
                    .statusCode(HttpStatus.OK.value());

            response = get(authGivenAnother(), DEFAULT_PATH + "/mine");
            then(response);
        }

        @DisplayName("예약 취소 - 승인 상태 - 사용자")
        @Test
        void should_cancel_approved_reservation() {
            patch(authGiven(), DEFAULT_PATH + "/2/approve");
            var response = patch(authGivenAnother(), DEFAULT_PATH + "/2/cancel");
            then(response)
                    .statusCode(HttpStatus.OK.value());

            response = get(authGivenAnother(), DEFAULT_PATH + "/mine");
            then(response);
        }

        @DisplayName("예약 취소 - 미승인 상태 - 관리자")
        @Test
        void should_cancel_unapproved_reservation_admin() {
            var response = patch(authGiven(), DEFAULT_PATH + "/2/cancel");
            then(response)
                    .statusCode(HttpStatus.OK.value());

            response = get(authGivenAnother(), DEFAULT_PATH + "/mine");
            then(response);
        }

        @DisplayName("예약 취소 - 승인 상태 - 관리자")
        @Test
        void should_cancel_approved_reservation_admin() {
            patch(authGiven(), DEFAULT_PATH + "/2/approve");
            var response = patch(authGiven(), DEFAULT_PATH + "/2/cancel");
            then(response)
                    .statusCode(HttpStatus.OK.value());

            response = get(authGivenAnother(), DEFAULT_PATH + "/mine");
            then(response);
        }
    }
}
