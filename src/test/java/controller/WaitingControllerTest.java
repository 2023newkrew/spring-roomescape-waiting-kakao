package controller;

import auth.domain.TokenData;
import io.restassured.specification.RequestSpecification;
import nextstep.etc.exception.ErrorMessage;
import nextstep.member.dto.MemberRequest;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.schedule.dto.ScheduleRequest;
import nextstep.theme.dto.ThemeRequest;
import nextstep.waiting.dto.WaitingRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

public class WaitingControllerTest extends AbstractControllerTest {

    static final String DEFAULT_PATH = "/reservation-waitings";

    @BeforeEach
    void setUp() {
        createMember();
        createTheme();
        createSchedule();
        createReservation();
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
        var request1 = new ScheduleRequest("2021-01-01", "00:00", 1L);
        post(given(), "/schedules", request1);
        var request2 = new ScheduleRequest("2021-01-01", "10:00", 1L);
        post(given(), "/schedules", request2);
    }

    private void createReservation() {
        var request = new ReservationRequest(1L);
        post(authGivenAnother(), "/reservations", request);
    }

    private RequestSpecification authGivenAnother() {
        String anotherToken = provider.createToken(new TokenData(2L, "ADMIN"));

        return given()
                .auth()
                .oauth2(anotherToken);
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class create {

        @DisplayName("예약 대기 생성 성공")
        @Test
        void should_returnLocation_when_givenRequest() {
            var request = createRequest(1L);

            var response = post(authGiven(), DEFAULT_PATH, request);

            then(response)
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", DEFAULT_PATH + "/1");
        }

        @DisplayName("같은 멤버, 스케줄의 예약 대기 생성 실패")
        @Test
        void should_throwException_when_duplicated() {
            var expectedException = ErrorMessage.WAITING_CONFLICT;
            var request = createRequest(1L);

            post(authGiven(), DEFAULT_PATH, request);
            var response = post(authGiven(), DEFAULT_PATH, request);

            thenThrow(response, expectedException);
        }

        @DisplayName("예약과 동일한 예약 대기 생성 실패")
        @Test
        void should_throwException_when_reservationExists() {
            var expectedException = ErrorMessage.RESERVATION_CONFLICT;
            var request = createRequest(2L);
            var reservationRequest = new ReservationRequest(request.getScheduleId());

            post(authGiven(), "/reservations", reservationRequest);
            var response = post(authGiven(), DEFAULT_PATH, request);

            thenThrow(response, expectedException);
        }

        @DisplayName("예약이 없을 경우 즉시 예약")
        @Test
        void should_returnReservationLocation_when_givenRequest() {
            var request = createRequest(2L);

            var response = post(authGiven(), DEFAULT_PATH, request);

            then(response)
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", "/reservations/2");
        }

        @DisplayName("스케줄이 없을 경우 예외 발생")
        @Test
        void should_thorwException_when_scheduleNotExists() {
            var expectedException = ErrorMessage.SCHEDULE_NOT_EXISTS;
            var request = createRequest(99L);

            var response = post(authGiven(), DEFAULT_PATH, request);

            thenThrow(response, expectedException);
        }
    }

    WaitingRequest createRequest(long scheduleId) {
        return new WaitingRequest(scheduleId);
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class delete {
        @DisplayName("자신의 예약 대기 취소")
        @Test
        void should_deleteWaiting_when_mine() {
            var request = createRequest(1L);

            post(authGiven(), DEFAULT_PATH, request);
            var response = delete(authGiven(), deletePath(1L));

            var result = response.as(Boolean.class);
            then(response).statusCode(HttpStatus.OK.value());
            Assertions.assertThat(result).isTrue();
        }

        String deletePath(Long id) {
            return DEFAULT_PATH + "/" + id;
        }

        @DisplayName("자신의 예약 대기가 아닌경우 예외 발생")
        @Test
        void should_throwException_when_not_mine() {
            var expectedException = ErrorMessage.NOT_WAITING_OWNER;
            var request = createRequest(1L);

            post(authGiven(), DEFAULT_PATH, request);
            var response = delete(authGivenAnother(), deletePath(1L));

            thenThrow(response, expectedException);
        }

        @DisplayName("예약 대기가 없는 경우 예외 발생")
        @Test
        void should_throwException_when_waitingNotExists() {
            var expectedException = ErrorMessage.WAITING_NOT_EXISTS;

            var response = delete(authGiven(), deletePath(1L));

            thenThrow(response, expectedException);
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class getByMemberId {

        static final String GET_BY_MEMBER_ID_PATH = DEFAULT_PATH + "/mine";

        @DisplayName("내 예약 대기 조회 성공")
        @Test
        void should_returnMember_when_memberExists() {
            var request = createRequest(1L);
            post(authGiven(), DEFAULT_PATH, request);

            var response = get(authGiven(), GET_BY_MEMBER_ID_PATH);

            then(response).statusCode(HttpStatus.OK.value());
        }
    }
}
