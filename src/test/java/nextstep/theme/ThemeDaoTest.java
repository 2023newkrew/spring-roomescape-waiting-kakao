package nextstep.theme;

import nextstep.domain.persist.Theme;
import nextstep.repository.ThemeDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class ThemeDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("테마 저장하기 테스트")
    void Should_IdIsNotNull_When_SaveTheme() {
        ThemeDao themeDao = new ThemeDao(jdbcTemplate);
        Long id = themeDao.save(new Theme("테마 이름", "테마 설명", 22_000));
        assertThat(id).isNotNull();
    }
}
