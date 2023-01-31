package nextstep.theme;

import nextstep.theme.model.Theme;
import nextstep.theme.util.ThemeTestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ThemeE2ETest {

    @DisplayName("인증 유무와 관계 없이 테마 목록을 조회할 수 았다.")
    @Test
    void test1() {
        List<Theme> themes = ThemeTestUtil.getThemes("2022-11-11");
        assertThat(themes).hasSize(2);
    }
}
