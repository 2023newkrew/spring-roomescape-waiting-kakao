package nextstep.auth;

import nextstep.auth.util.AuthTestUtil;
import nextstep.member.util.MemberTestUtil;
import nextstep.schedule.util.ScheduleTestUtil;
import nextstep.schedule.model.ScheduleRequest;
import nextstep.theme.util.ThemeTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminE2ETest {
    private String ROLE_MEMBER_ACCESS_TOKEN;
    private String ROLE_ADMIN_ACCESS_TOKEN;

    @BeforeEach
    void setUp(){
        ROLE_ADMIN_ACCESS_TOKEN  = AuthTestUtil.tokenLogin(MemberTestUtil.ROLE_ADMIN_MEMBER).getAccessToken();
        ROLE_MEMBER_ACCESS_TOKEN = AuthTestUtil.tokenLogin(MemberTestUtil.ROLE_MEMBER_MEMBER).getAccessToken();
    }

    @DisplayName("Admin 권한을 가진 사용자는 테마를 생성할 수 있다.")
    @Test
    void test1() {
        ThemeTestUtil.createThemeAndGetValidatableResponse(ThemeTestUtil.DEFAULT_THEME_REQUEST, ROLE_ADMIN_ACCESS_TOKEN)
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("Admin 권한을 가지지 않은 사용자는 테마를 생성할 수 없다.")
    @Test
    void test2() {
        ThemeTestUtil.createThemeAndGetValidatableResponse(ThemeTestUtil.DEFAULT_THEME_REQUEST, ROLE_MEMBER_ACCESS_TOKEN)
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("Admin 권한을 가진 사용자는 테마를 삭제할 수 있다.")
    @Test
    void test3() {
        ThemeTestUtil.deleteThemeAndGetValidatableResponse(1L, ROLE_ADMIN_ACCESS_TOKEN)
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("Admin 권한을 가지지 않은 사용자는 테마를 삭제할 수 없다.")
    @Test
    void test4() {
        ThemeTestUtil.deleteThemeAndGetValidatableResponse(1L, ROLE_MEMBER_ACCESS_TOKEN)
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("Admin 권한을 가진 사용자는 스케줄을 생성할 수 있다.")
    @Test
    void test5() {
        ScheduleRequest schedule = new ScheduleRequest(1L, "2022-08-11", "13:00");
        ScheduleTestUtil.createScheduleAndGetValidatableResponse(schedule, ROLE_ADMIN_ACCESS_TOKEN)
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("Admin 권한을 가지지 않은 사용자는 스케줄을 생성할 수 없다.")
    @Test
    void test6() {
        ScheduleRequest schedule = new ScheduleRequest(1L, "2022-08-11", "13:00");
        ScheduleTestUtil.createScheduleAndGetValidatableResponse(schedule, ROLE_MEMBER_ACCESS_TOKEN)
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("Admin 권한을 가진 사용자는 스케줄을 삭제할 수 있다.")
    @Test
    void test7() {
        ScheduleTestUtil.deleteScheduleAndGetValidatableResponse(2L, ROLE_ADMIN_ACCESS_TOKEN)
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("Admin 권한을 가지지 않은 사용자는 스케줄을 삭제할 수 없다.")
    @Test
    void test8() {
        ScheduleTestUtil.deleteScheduleAndGetValidatableResponse(2L, ROLE_MEMBER_ACCESS_TOKEN)
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}
