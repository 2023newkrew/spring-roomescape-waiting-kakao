package controller;

import auth.provider.JwtTokenProvider;
import nextstep.schedule.dto.ScheduleRequest;
import nextstep.schedule.exception.ScheduleErrorMessage;
import nextstep.theme.exception.ThemeErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;

public class ScheduleControllerTest extends AbstractControllerTest {

    static final String DEFAULT_PATH = "/schedules";

    @Autowired
    JwtTokenProvider provider;

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class create {

        static final String CREATE_PATH = "/admin/schedules";

        @DisplayName("스케줄 생성 성공")
        @Test
        void should_returnLocation_when_givenRequest() {
            var request = createRequest();

            var response = post(authGiven(), CREATE_PATH, request);

            then(response)
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", DEFAULT_PATH + "/2");
        }

        @DisplayName("동일한 스케줄이 존재할 경우 예외 발생")
        @Test
        void should_throwException_when_scheduleDuplicate() {
            var expectedException = ScheduleErrorMessage.CONFLICT;
            var request = createRequest();

            post(authGiven(), CREATE_PATH, request);
            var response = post(authGiven(), CREATE_PATH, request);

            thenThrow(response, expectedException);
        }

        @DisplayName("테마가 없는 경우 예외 발생")
        @Test
        void should_throwException_when_themeNotExists() {
            var expectedException = ThemeErrorMessage.NOT_EXISTS;
            var request = createRequest();
            request = new ScheduleRequest(request.getDate(), request.getTime(), 2L);

            var response = post(authGiven(), CREATE_PATH, request);

            thenThrow(response, expectedException);
        }


        @DisplayName("입력이 포맷에 맞지 않는 경우 예외 발생")
        @ParameterizedTest
        @MethodSource
        void should_throwException_when_invalidRequest(ScheduleRequest request) {
            var response = post(authGiven(), CREATE_PATH, request);

            then(response)
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }


        List<Arguments> should_throwException_when_invalidRequest() {
            var request = createRequest();

            return List.of(
                    Arguments.of(new ScheduleRequest(null, null, null)),
                    Arguments.of(new ScheduleRequest(null, request.getTime(), request.getThemeId())),
                    Arguments.of(new ScheduleRequest(request.getDate(), null, request.getThemeId())),
                    Arguments.of(new ScheduleRequest(request.getDate(), request.getTime(), null))
            );
        }
    }

    ScheduleRequest createRequest() {
        return new ScheduleRequest(LocalDate.of(2022, 8, 11), LocalTime.of(11, 0), 1L);
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class getById {

        @DisplayName("스케줄 조회 성공")
        @Test
        void should_returnMember_when_memberExists() {
            var response = get(given(), DEFAULT_PATH + "/1");

            then(response)
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(1))
                    .body("date", equalTo(List.of(2023, 1, 1)))
                    .body("time", equalTo(List.of(0, 0)))
                    .body("theme.id", equalTo(1));
        }

        @DisplayName("스케줄이 없을 경우 빈 body 반환")
        @Test
        void should_returnNull_when_memberNotExists() {
            var response = get(given(), DEFAULT_PATH + "/2");

            then(response)
                    .statusCode(HttpStatus.OK.value())
                    .content(emptyString());
        }
    }
}
