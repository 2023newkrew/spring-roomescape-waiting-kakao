package controller;

import com.authorizationserver.infrastructures.jwt.JwtTokenProvider;
import com.nextstep.domains.exceptions.ErrorMessageType;
import com.nextstep.interfaces.schedule.dtos.ScheduleRequest;
import com.nextstep.interfaces.theme.dtos.ThemeRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;

public class ScheduleControllerTest extends AbstractControllerTest {

    static final String DEFAULT_PATH = "/schedules";

    @Autowired
    JwtTokenProvider provider;

    @BeforeEach
    void setUp() {
        var request = new ThemeRequest("theme", "theme", 10000);
        post(authGiven(), "/admin/themes", request);
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class create {

        @DisplayName("스케줄 생성 성공")
        @Test
        void should_returnLocation_when_givenRequest() {
            var request = createRequest();


            var response = post(given(), DEFAULT_PATH, request);

            then(response)
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", DEFAULT_PATH + "/1");
        }

        @DisplayName("동일한 스케줄이 존재할 경우 예외 발생")
        @Test
        void should_throwException_when_scheduleDuplicate() {
            var expectedException = ErrorMessageType.SCHEDULE_CONFLICT;
            var request = createRequest();

            post(given(), DEFAULT_PATH, request);
            var response = post(given(), DEFAULT_PATH, request);

            thenThrow(response, expectedException);
        }

        @DisplayName("테마가 없는 경우 예외 발생")
        @Test
        void should_throwException_when_themeNotExists() {
            var expectedException = ErrorMessageType.THEME_NOT_EXISTS;
            var request = createRequest();
            request = new ScheduleRequest(request.getDate(), request.getTime(), 2L);

            var response = post(given(), DEFAULT_PATH, request);

            thenThrow(response, expectedException);
        }


        @DisplayName("입력이 포맷에 맞지 않는 경우 예외 발생")
        @ParameterizedTest
        @MethodSource
        void should_throwException_when_invalidRequest(ScheduleRequest request) {
            var response = post(given(), DEFAULT_PATH, request);

            then(response)
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }


        List<Arguments> should_throwException_when_invalidRequest() {
            var request = createRequest();

            return List.of(
                    Arguments.of(new ScheduleRequest()),
                    Arguments.of(new ScheduleRequest(null, request.getTime(), request.getThemeId())),
                    Arguments.of(new ScheduleRequest(request.getDate(), null, request.getThemeId())),
                    Arguments.of(new ScheduleRequest(request.getDate(), request.getTime(), null)),
                    Arguments.of(new ScheduleRequest("2021-13-31", request.getTime(), request.getThemeId())),
                    Arguments.of(new ScheduleRequest(request.getDate(), "25:00", request.getThemeId()))
            );
        }
    }

    ScheduleRequest createRequest() {
        return new ScheduleRequest("2022-08-11", "13:00", 1L);
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class getById {

        @DisplayName("스케줄 조회 성공")
        @Test
        void should_returnMember_when_memberExists() {
            var request = createRequest();
            post(given(), DEFAULT_PATH, request);

            var response = get(given(), DEFAULT_PATH + "/1");

            then(response)
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(1))
                    .body("date", equalTo(List.of(2022, 8, 11)))
                    .body("time", equalTo(List.of(13, 0)))
                    .body("theme.id", equalTo(1));
        }

        @DisplayName("스케줄이 없을 경우 빈 body 반환")
        @Test
        void should_returnNull_when_memberNotExists() {
            var response = get(given(), DEFAULT_PATH + "/1");

            then(response)
                    .statusCode(HttpStatus.OK.value())
                    .content(emptyString());
        }
    }
}
