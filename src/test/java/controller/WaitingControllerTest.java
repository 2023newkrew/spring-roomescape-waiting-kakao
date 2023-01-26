package controller;

import nextstep.etc.exception.ErrorMessage;
import nextstep.member.dto.MemberRequest;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.schedule.dto.ScheduleRequest;
import nextstep.theme.dto.ThemeRequest;
import nextstep.waiting.dto.WaitingRequest;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

public class WaitingControllerTest extends AbstractControllerTest {

    static final String DEFAULT_PATH = "/reservation-waitings";

    @BeforeEach
    void setUp() {
        createMember();
        createTheme();
        createSchedule();
    }

    private void createTheme() {
        var request = new ThemeRequest("theme", "theme", 10000);
        post(authGiven(), "/admin/themes", request);
    }

    private void createMember() {
        var request = new MemberRequest("member", "password", "member", "-");
        post(given(), "/members", request);
    }

    private void createSchedule() {
        var request = new ScheduleRequest("2021-01-01", "00:00", 1L);
        post(given(), "/schedules", request);
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class create {

        @DisplayName("예약 대기 생성 성공")
        @Test
        void should_returnLocation_when_givenRequest() {
            var request = createRequest();

            var response = post(authGiven(), DEFAULT_PATH, request);

            then(response)
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", DEFAULT_PATH + "/1");
        }

        @DisplayName("같은 멤버, 스케줄의 예약 대기 생성 실패")
        @Test
        void should_throwException_when_duplicated() {
            var expectedException = ErrorMessage.WAITING_CONFLICT;
            var request = createRequest();

            post(authGiven(), DEFAULT_PATH, request);
            var response = post(authGiven(), DEFAULT_PATH, request);

            thenThrow(response, expectedException);
        }

        @DisplayName("예약과 동일한 예약 대기 생성 실패")
        @Test
        void should_throwException_when_reservationExists() {
            var expectedException = ErrorMessage.RESERVATION_CONFLICT;
            var request = createRequest();
            var reservationRequest = new ReservationRequest(request.getScheduleId());

            post(authGiven(), "/reservations", reservationRequest);
            var response = post(authGiven(), DEFAULT_PATH, request);

            thenThrow(response, expectedException);
        }
    }

    WaitingRequest createRequest() {
        return new WaitingRequest(1L);
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class getById {

        //        @DisplayName("멤버 조회 성공")
        //        @Test
        //        void should_returnMember_when_memberExists() {
        //            var request = createRequest();
        //            post(WaitingControllerTest.this.given(), DEFAULT_PATH, request);
        //
        //            var response = get(WaitingControllerTest.this.given(), DEFAULT_PATH + "/1");
        //
        //            then(response)
        //                    .statusCode(HttpStatus.OK.value())
        //                    .body("id", equalTo(1))
        //                    .body("username", equalTo(request.getUsername()))
        //                    .body("password", equalTo(request.getPassword()))
        //                    .body("name", equalTo(request.getName()))
        //                    .body("phone", equalTo(request.getPhone()))
        //                    .body("role", equalTo("NORMAL"));
        //        }
        //
        //        @DisplayName("멤버가 없을 경우 빈 body 반환")
        //        @Test
        //        void should_returnNull_when_memberNotExists() {
        //            var response = get(WaitingControllerTest.this.given(), DEFAULT_PATH + "/1");
        //
        //            then(response)
        //                    .statusCode(HttpStatus.OK.value())
        //                    .content(emptyString());
        //        }
    }
}
