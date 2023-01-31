package nextstep.schedule;

import nextstep.schedule.model.Schedule;
import nextstep.schedule.util.ScheduleTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ScheduleE2ETest {

    @DisplayName("인증되지 않은 사용자는 스케줄을 조회할 수 있다.")
    @Test
    void test1() {
        List<Schedule> schedules = ScheduleTestUtil.getSchedules(1L, "2022-11-11");

        assertThat(schedules).hasSize(6);
    }
}
