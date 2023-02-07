package controller;

import auth.dto.TokenRequest;
import nextstep.member.dto.MemberRequest;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.schedule.dto.ScheduleRequest;
import nextstep.schedule.exception.ScheduleErrorMessage;
import nextstep.waiting.exception.WaitingErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class WaitingControllerTest extends AbstractControllerTest {

    static final String DEFAULT_PATH = "/reservation-waitings";

    String anotherToken;

    @Override
    void setUpTemplate() {
        super.setUpTemplate();

        createMember();
        createSchedule();
        createReservation();
    }

    private void createMember() {
        var request = new MemberRequest("member", "password", "member", "-");
        post(given(), "/members", request);
        var tokenRequest = new TokenRequest(request.getUsername(), request.getPassword());
        anotherToken = createProvider(tokenRequest).createToken(tokenRequest).getAccessToken();
    }

    private void createSchedule() {
        var request = new ScheduleRequest(LocalDate.of(2021, 1, 1), LocalTime.of(10, 0), 1L);
        post(authGiven(), "/admin/schedules", request);
    }

    private void createReservation() {
        var request = new ReservationRequest(1L);
        post(authGiven(anotherToken), "/reservations", request);
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
            var expectedException = WaitingErrorMessage.CONFLICT;
            var request = createRequest(1L);

            post(authGiven(), DEFAULT_PATH, request);
            var response = post(authGiven(), DEFAULT_PATH, request);

            thenThrow(response, expectedException);
        }

        @DisplayName("예약과 동일한 예약 대기 생성 실패")
        @Test
        void should_throwException_when_reservationExists() {
            var expectedException = WaitingErrorMessage.ALREADY_RESERVED;
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
            var expectedException = ScheduleErrorMessage.NOT_EXISTS;
            var request = createRequest(99L);

            var response = post(authGiven(), DEFAULT_PATH, request);

            thenThrow(response, expectedException);
        }
    }

    ReservationRequest createRequest(long scheduleId) {
        return new ReservationRequest(scheduleId);
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
            var expectedException = WaitingErrorMessage.NOT_OWNER;
            var request = createRequest(1L);

            post(authGiven(), DEFAULT_PATH, request);
            var response = delete(authGiven(anotherToken), deletePath(1L));

            thenThrow(response, expectedException);
        }

        @DisplayName("예약 대기가 없는 경우 예외 발생")
        @Test
        void should_throwException_when_waitingNotExists() {
            var expectedException = WaitingErrorMessage.NOT_EXISTS;

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
