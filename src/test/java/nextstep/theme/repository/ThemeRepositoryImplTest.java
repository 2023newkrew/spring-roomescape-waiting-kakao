package nextstep.theme.repository;

import nextstep.theme.domain.Theme;
import nextstep.theme.repository.jdbc.ThemeResultSetParser;
import nextstep.theme.repository.jdbc.ThemeStatementCreator;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;
import java.util.Objects;

@SqlGroup(
        {
                @Sql("classpath:/dropTable.sql"),
                @Sql("classpath:/schema.sql")
        })
@JdbcTest
class ThemeRepositoryImplTest {

    final JdbcTemplate jdbcTemplate;

    final ThemeStatementCreator statementCreator;

    final ThemeResultSetParser resultSetParser;

    final ThemeRepository repository;


    @Autowired
    public ThemeRepositoryImplTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.statementCreator = new ThemeStatementCreator();
        this.resultSetParser = new ThemeResultSetParser();
        this.repository = new ThemeRepositoryImpl(jdbcTemplate, statementCreator, resultSetParser);
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class insert {

        @DisplayName("호출 횟수만큼 ID가 증가하는지 확인")
        @Test
        void should_increaseId_when_insertTwice() {
            Theme theme1 = new Theme(null, "theme1", "desc", 1000);
            Theme theme2 = new Theme(null, "theme2", "desc", 1000);

            Long id1 = repository.insert(theme1)
                    .getId();
            Long id2 = repository.insert(theme2)
                    .getId();

            Assertions.assertThat(id1 + 1L)
                    .isEqualTo(id2);
        }

        @DisplayName("이름이 중복될 경우 예외 발생")
        @Test
        void should_throwException_when_nameDuplicated() {
            Theme theme = new Theme(null, "theme", "desc", 1000);
            repository.insert(theme);

            Assertions.assertThatThrownBy(() -> repository.insert(theme))
                    .isInstanceOf(DuplicateKeyException.class);
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class getById {

        @BeforeEach
        void setUp() {
            List<Theme> themes = List.of(
                    new Theme(null, "theme1", "theme1", 1000),
                    new Theme(null, "theme2", "theme2", 2000),
                    new Theme(null, "theme3", "theme3", 3000)
            );
            themes.forEach(ThemeRepositoryImplTest.this::insertTestTheme);
        }

        @DisplayName("ID와 일치하는 예약 확인")
        @ParameterizedTest
        @MethodSource
        void should_returnTheme_when_givenId(Long id, Theme theme) {
            Theme actual = repository.getById(id);

            Assertions.assertThat(actual)
                    .extracting(
                            Theme::getId,
                            Theme::getName,
                            Theme::getDesc,
                            Theme::getPrice
                    )
                    .contains(
                            id,
                            theme.getName(),
                            theme.getDesc(),
                            theme.getPrice()
                    );
        }


        List<Arguments> should_returnTheme_when_givenId() {
            return List.of(
                    Arguments.of(1L, new Theme(null, "theme1", "theme1", 1000)),
                    Arguments.of(2L, new Theme(null, "theme2", "theme2", 2000)),
                    Arguments.of(3L, new Theme(null, "theme3", "theme3", 3000))
            );
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class getAll {

        @DisplayName("테마 목록을 전부 불러오는지 확인")
        @Test
        void should_returnAllThemes_when_givenThemes() {
            List<Theme> themes = List.of(
                    new Theme(null, "theme1", "theme1", 1000),
                    new Theme(null, "theme2", "theme2", 2000),
                    new Theme(null, "theme3", "theme3", 3000)
            );
            themes.forEach(ThemeRepositoryImplTest.this::insertTestTheme);

            List<Theme> actual = repository.getAll();

            Assertions.assertThatCollection(actual)
                    .allSatisfy(theme -> Assertions.assertThat(theme)
                            .is(containsTo(themes)));
        }
    }

    Condition<Theme> containsTo(List<Theme> themes) {
        return new Condition<>(
                actual -> themes.stream()
                        .anyMatch(theme -> equals(theme, actual)),
                "containsTo"
        );
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class update {

        @BeforeEach
        void setUp() {
            List<Theme> themes = List.of(
                    new Theme(null, "theme1", "theme1", 1000),
                    new Theme(null, "theme2", "theme2", 2000)
            );
            themes.forEach(ThemeRepositoryImplTest.this::insertTestTheme);
        }

        @DisplayName("수정 내용이 db에 반영되는지 확인")
        @ParameterizedTest
        @MethodSource
        void should_updateTheme_when_givenIdAndTheme(Long id, Theme theme) {
            repository.update(id, theme);

            Theme actual = getThemeById(id);

            Assertions.assertThat(actual)
                    .extracting(
                            Theme::getId,
                            Theme::getName,
                            Theme::getDesc,
                            Theme::getPrice
                    )
                    .contains(
                            id,
                            theme.getName(),
                            theme.getDesc(),
                            theme.getPrice()
                    );
        }

        List<Arguments> should_updateTheme_when_givenIdAndTheme() {
            return List.of(
                    Arguments.of(1L, new Theme(null, "updated1", "updated", 1000)),
                    Arguments.of(1L, new Theme(null, "updated2", null, 2000)),
                    Arguments.of(2L, new Theme(null, "updated3", "updated", 3000)),
                    Arguments.of(2L, new Theme(null, "updated4", null, 3000))
            );
        }

        @DisplayName("수정 성공 여부 확인")
        @ParameterizedTest
        @CsvSource(value = {"0,false", "1, true", "2, true", "3, false"})
        void should_returnUpdated_when_givenId(Long id, boolean updated) {
            Theme theme = new Theme(null, "updated", "updated", 0);

            boolean actual = repository.update(id, theme);

            Assertions.assertThat(actual)
                    .isEqualTo(updated);
        }

        @DisplayName("이름이 중복될 경우 예외 발생")
        @Test
        void should_throwException_when_nameDuplicated() {
            Theme theme = new Theme(null, "theme2", "theme2", 2000);

            Assertions.assertThatThrownBy(() -> repository.update(1L, theme))
                    .isInstanceOf(DuplicateKeyException.class);
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class delete {

        @BeforeEach
        void setUp() {
            List<Theme> themes = List.of(
                    new Theme(null, "theme1", "theme1", 1000),
                    new Theme(null, "theme2", "theme2", 2000)
            );
            themes.forEach(ThemeRepositoryImplTest.this::insertTestTheme);
        }

        @DisplayName("삭제 성공 여부 확인")
        @ParameterizedTest
        @CsvSource(value = {"0,false", "1, true", "2, true", "3, false"})
        void should_returnDeleted_when_givenId(Long id, boolean deleted) {
            boolean actual = repository.delete(id);

            Assertions.assertThat(actual)
                    .isEqualTo(deleted);
        }
    }

    private void insertTestTheme(Theme theme) {
        jdbcTemplate.update(connection -> statementCreator.createInsert(connection, theme));
    }

    private Theme getThemeById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseSingleTheme
        );
    }

    boolean equals(Theme expected, Theme actual) {
        return Objects.equals(expected.getName(), actual.getName()) &&
                Objects.equals(expected.getDesc(), actual.getDesc()) &&
                Objects.equals(expected.getPrice(), actual.getPrice());
    }
}