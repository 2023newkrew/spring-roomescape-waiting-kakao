package nextstep.theme;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class ThemeDaoTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void save() {
        ThemeDao themeDao = new ThemeDao(dataSource);
        Long id = themeDao.save(Theme.builder().name("테마 이름").desc("테마 설명").price(22_000).build());
        assertThat(id).isNotNull();
    }
}
