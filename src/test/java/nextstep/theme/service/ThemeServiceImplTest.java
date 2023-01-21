package nextstep.theme.service;

import nextstep.etc.exception.ErrorMessage;
import nextstep.etc.exception.ThemeException;
import nextstep.theme.domain.Theme;
import nextstep.theme.dto.ThemeRequest;
import nextstep.theme.dto.ThemeResponse;
import nextstep.theme.mapper.ThemeMapper;
import nextstep.theme.repository.FakeThemeRepository;
import nextstep.theme.repository.ThemeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.List;

class ThemeServiceImplTest {

    ThemeRepository repository;

    ThemeService service;


    @BeforeEach
    void setUp() {
        this.repository = new FakeThemeRepository();
        this.service = new ThemeServiceImpl(repository, Mappers.getMapper(ThemeMapper.class));
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class create {

        @DisplayName("생성 성공")
        @ParameterizedTest
        @MethodSource
        void should_returnResponse_when_givenRequest(ThemeRequest request) {
            var actual = service.create(request);

            Assertions.assertThat(actual)
                    .extracting(
                            ThemeResponse::getId,
                            ThemeResponse::getName,
                            ThemeResponse::getDesc,
                            ThemeResponse::getPrice
                    )
                    .contains(
                            1L,
                            request.getName(),
                            request.getDesc(),
                            request.getPrice()
                    );
        }

        List<Arguments> should_returnResponse_when_givenRequest() {
            return List.of(
                    Arguments.of(new ThemeRequest("theme1", "theme1", 1000)),
                    Arguments.of(new ThemeRequest("theme2", "theme2", 2000)),
                    Arguments.of(new ThemeRequest("theme3", "theme3", 3000))
            );
        }

        @DisplayName("이름이 중복될 경우 예외 발생")
        @Test
        void should_throwException_when_nameDuplicated() {
            var request = new ThemeRequest("theme1", "theme1", 1000);
            service.create(request);

            Assertions.assertThatThrownBy(() -> service.create(request))
                    .isInstanceOf(ThemeException.class)
                    .hasMessage(ErrorMessage.THEME_CONFLICT.getErrorMessage());
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class getById {

        @BeforeEach
        void setUp() {
            repository.insert(new Theme(null, "theme1", "theme1", 1000));
            repository.insert(new Theme(null, "theme2", "theme2", 2000));
            repository.insert(new Theme(null, "theme3", "theme3", 3000));
        }

        @DisplayName("id와 일치하는 theme를 반환")
        @ParameterizedTest
        @MethodSource
        void should_returnResponse_when_givenId(Long id, ThemeResponse response) {
            var actual = service.getById(id);

            Assertions.assertThat(actual)
                    .isEqualTo(response);
        }

        List<Arguments> should_returnResponse_when_givenId() {
            return List.of(
                    Arguments.of(0L, null),
                    Arguments.of(1L, new ThemeResponse(1L, "theme1", "theme1", 1000)),
                    Arguments.of(2L, new ThemeResponse(2L, "theme2", "theme2", 2000)),
                    Arguments.of(3L, new ThemeResponse(3L, "theme3", "theme3", 3000)),
                    Arguments.of(4L, null)
            );
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class getAll {

        @BeforeEach
        void setUp() {
            repository.insert(new Theme(null, "theme1", "theme1", 1000));
            repository.insert(new Theme(null, "theme2", "theme2", 2000));
            repository.insert(new Theme(null, "theme3", "theme3", 3000));
        }

        @DisplayName("모든 theme를 반환")
        @Test
        void should_returnResponses_when_existThemes() {
            var actual = service.getAll();

            Assertions.assertThatCollection(actual)
                    .hasSize(3)
                    .contains(
                            new ThemeResponse(1L, "theme1", "theme1", 1000),
                            new ThemeResponse(2L, "theme2", "theme2", 2000),
                            new ThemeResponse(3L, "theme3", "theme3", 3000)
                    );
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class update {

        @BeforeEach
        void setUp() {
            repository.insert(new Theme(null, "theme1", "theme1", 1000));
            repository.insert(new Theme(null, "theme2", "theme2", 2000));
        }

        @DisplayName("수정 성공")
        @ParameterizedTest
        @MethodSource
        void should_returnResponse_when_givenId(Long id, ThemeResponse response) {
            var request = new ThemeRequest("updated", "updated", 0);

            var actual = service.update(id, request);

            Assertions.assertThat(actual)
                    .isEqualTo(response);
        }

        List<Arguments> should_returnResponse_when_givenId() {
            return List.of(
                    Arguments.of(1L, new ThemeResponse(1L, "updated", "updated", 0)),
                    Arguments.of(2L, new ThemeResponse(2L, "updated", "updated", 0))
            );
        }

        @DisplayName("이름이 중복될 경우 예외 발생")
        @Test
        void should_throwException_when_nameDuplicated() {
            var request = new ThemeRequest("theme2", "theme2", 2000);

            Assertions.assertThatThrownBy(() -> service.update(1L, request))
                    .isInstanceOf(ThemeException.class)
                    .hasMessage(ErrorMessage.THEME_CONFLICT.getErrorMessage());
        }

        @DisplayName("수정할 테마가 존재하지 않을 경우 예외 발생")
        @Test
        void should_throwException_when_themeNotExists() {
            var request = new ThemeRequest("theme3", "theme2", 2000);

            Assertions.assertThatThrownBy(() -> service.update(0L, request))
                    .isInstanceOf(ThemeException.class)
                    .hasMessage(ErrorMessage.THEME_NOT_EXISTS.getErrorMessage());
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class delete {

        @BeforeEach
        void setUp() {
            repository.insert(new Theme(null, "theme1", "theme1", 1000));
            repository.insert(new Theme(null, "theme2", "theme2", 2000));
        }

        @DisplayName("삭제 성공 여부 확인")
        @ParameterizedTest
        @CsvSource(value = {"0,false", "1, true", "2, true", "3, false"})
        void should_returnDeleted_when_givenId(Long id, boolean deleted) {
            boolean actual = service.deleteById(id);

            Assertions.assertThat(actual)
                    .isEqualTo(deleted);
        }

        @DisplayName("예약이 존재하는 테마를 제거할 경우 예외 발생")
        @Test
        void should_throwException_when_reservationExists() {
            Assertions.assertThatThrownBy(() -> service.deleteById(-1L))
                    .isInstanceOf(ThemeException.class)
                    .hasMessage(ErrorMessage.THEME_RESERVATION_EXISTS.getErrorMessage());
        }
    }
}