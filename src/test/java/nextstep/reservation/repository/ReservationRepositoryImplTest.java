package nextstep.reservation.repository;

import nextstep.reservation.domain.Reservation;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.mapper.ReservationMapper;
import nextstep.reservation.repository.jdbc.ReservationResultSetParser;
import nextstep.reservation.repository.jdbc.ReservationStatementCreator;
import nextstep.theme.domain.Theme;
import nextstep.theme.repository.jdbc.ThemeStatementCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

@SqlGroup(
        {
                @Sql("classpath:/dropTable.sql"),
                @Sql("classpath:/schema.sql"),
                @Sql("classpath:/data.sql")
        })
@JdbcTest
class ReservationRepositoryImplTest {

    final JdbcTemplate jdbcTemplate;

    final ReservationStatementCreator reservationStatementCreator;

    final ThemeStatementCreator themeStatementCreator;

    final ReservationResultSetParser resultSetParser;

    final ReservationRepository repository;

    final ReservationMapper mapper;

    @Autowired
    public ReservationRepositoryImplTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationStatementCreator = new ReservationStatementCreator();
        this.themeStatementCreator = new ThemeStatementCreator();
        this.resultSetParser = new ReservationResultSetParser();
        this.repository = new ReservationRepositoryImpl(jdbcTemplate, reservationStatementCreator, resultSetParser);
        this.mapper = Mappers.getMapper(ReservationMapper.class);
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class existsByDateAndTime {

        @BeforeEach
        void setUp() {
            List<ReservationRequest> requests = List.of(
                    new ReservationRequest("2022-08-12", "13:00", "류성현", 1L),
                    new ReservationRequest("2022-08-11", "14:00", "류성현", 1L),
                    new ReservationRequest("2022-08-11", "13:00", "pluto", 1L)
            );
            requests.forEach(request -> insertReservation(mapper.fromRequest(request)));
            insertTheme(new Theme(null, "test", "test", 1000));
        }

        @DisplayName("date와 time이 같은 예약이 존재하는지 확인")
        @ParameterizedTest
        @MethodSource
        void should_returnExists_when_givenRequest(ReservationRequest request, boolean exists) {
            boolean actual = repository.existsByTimetable(mapper.fromRequest(request));

            Assertions.assertThat(actual)
                    .isEqualTo(exists);
        }

        List<Arguments> should_returnExists_when_givenRequest() {
            return List.of(
                    Arguments.of(new ReservationRequest("2022-08-11", "14:00", "류성현", 1L), true),
                    Arguments.of(new ReservationRequest("2022-08-12", "13:00", "pluto", 1L), true),
                    Arguments.of(new ReservationRequest("2022-08-11", "16:00", "류성현", 1L), false),
                    Arguments.of(new ReservationRequest("2022-08-13", "13:00", "pluto", 1L), false)
            );
        }

        @DisplayName("삽입 후 중복여부 확인")
        @ParameterizedTest
        @MethodSource
        void should_returnFalse_when_afterInsert(ReservationRequest request) {
            Reservation reservation = mapper.fromRequest(request);

            boolean before = repository.existsByTimetable(reservation);
            insertReservation(mapper.fromRequest(request));
            boolean after = repository.existsByTimetable(reservation);

            Assertions.assertThat(before)
                    .isFalse();
            Assertions.assertThat(after)
                    .isTrue();
        }

        List<Arguments> should_returnFalse_when_afterInsert() {
            return List.of(
                    Arguments.of(new ReservationRequest("2023-08-11", "14:00", "류성현", 1L), 1L),
                    Arguments.of(new ReservationRequest("2023-08-12", "13:00", "pluto", 1L), 2L),
                    Arguments.of(new ReservationRequest("2023-08-11", "16:00", "류성현", 1L), 3L),
                    Arguments.of(new ReservationRequest("2023-08-13", "13:00", "pluto", 1L), 4L)
            );
        }

        @DisplayName("테마가 다를 경우 삽입 가능")
        @Test
        void should_returnFalse_when_diffTheme() {
            ReservationRequest request = new ReservationRequest("2022-08-11", "13:00", "pluto", 2L);
            Reservation reservation = mapper.fromRequest(request);

            boolean actual = repository.existsByTimetable(reservation);

            Assertions.assertThat(actual)
                    .isFalse();
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class insert {

        @DisplayName("호출 횟수만큼 ID가 증가하는지 확인")
        @Test
        void should_increaseId_when_insertTwice() {
            ReservationRequest request = new ReservationRequest("2022-08-11", "14:00", "류성현", 1L);
            Reservation reservation = mapper.fromRequest(request);

            Long id1 = repository.insert(reservation)
                    .getId();
            Long id2 = repository.insert(reservation)
                    .getId();

            Assertions.assertThat(id1 + 1L)
                    .isEqualTo(id2);
        }

        @DisplayName("저장된 예약이 요청한 값이 맞는지 확인")
        @ParameterizedTest
        @MethodSource
        void should_returnReservation_when_givenRequest(ReservationRequest request) {
            Reservation reservation = mapper.fromRequest(request);

            Reservation actual = repository.insert(reservation);

            Assertions.assertThat(actual)
                    .extracting(
                            Reservation::getDate,
                            Reservation::getTime,
                            Reservation::getName
                    )
                    .contains(
                            reservation.getDate(),
                            reservation.getTime(),
                            reservation.getName()
                    );
        }

        List<Arguments> should_returnReservation_when_givenRequest() {
            return List.of(
                    Arguments.of(new ReservationRequest("2022-08-11", "14:00", "류성현", 1L)),
                    Arguments.of(new ReservationRequest("2022-08-12", "13:00", "pluto", 1L)),
                    Arguments.of(new ReservationRequest("2022-08-11", "16:00", "류성현", 1L)),
                    Arguments.of(new ReservationRequest("2022-08-13", "13:00", "pluto", 1L))
            );
        }

        @DisplayName("테마가 존재하지 않을 경우 예외 발생")
        @Test
        void should_throwException_when_themeNotExists() {
            ReservationRequest request = new ReservationRequest("2022-08-11", "14:00", "류성현", 0L);
            Reservation reservation = mapper.fromRequest(request);

            Assertions.assertThatThrownBy(() -> repository.insert(reservation))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class getById {

        @BeforeEach
        void setUp() {
            List<ReservationRequest> requests = List.of(
                    new ReservationRequest("2022-08-12", "13:00", "류성현", 1L),
                    new ReservationRequest("2022-08-11", "14:00", "류성현", 1L),
                    new ReservationRequest("2022-08-11", "13:00", "pluto", 1L)
            );
            requests.forEach(request -> insertReservation(mapper.fromRequest(request)));
        }

        @DisplayName("ID와 일치하는 예약 확인")
        @ParameterizedTest
        @MethodSource
        void should_returnReservation_when_givenId(Long id, ReservationRequest request) {
            Reservation reservation = mapper.fromRequest(request);

            Reservation actual = repository.getById(id);

            Assertions.assertThat(actual)
                    .extracting(
                            Reservation::getId,
                            Reservation::getDate,
                            Reservation::getTime,
                            Reservation::getName
                    )
                    .contains(
                            id,
                            reservation.getDate(),
                            reservation.getTime(),
                            reservation.getName()
                    );
        }


        List<Arguments> should_returnReservation_when_givenId() {
            return List.of(
                    Arguments.of(1L, new ReservationRequest("2022-08-12", "13:00", "류성현", 1L)),
                    Arguments.of(2L, new ReservationRequest("2022-08-11", "14:00", "류성현", 1L)),
                    Arguments.of(3L, new ReservationRequest("2022-08-11", "13:00", "pluto", 1L))
            );
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class deleteById {

        @BeforeEach
        void setUp() {
            List<ReservationRequest> requests = List.of(
                    new ReservationRequest("2022-08-12", "13:00", "류성현", 1L),
                    new ReservationRequest("2022-08-11", "14:00", "류성현", 1L),
                    new ReservationRequest("2022-08-11", "13:00", "pluto", 1L)
            );
            requests.forEach(request -> insertReservation(mapper.fromRequest(request)));
        }

        @DisplayName("예약이 존재하면 제거하고 결과 반환")
        @ParameterizedTest
        @CsvSource({"1, true", "2, true", "4, false"})
        void should_returnIsDeleted_when_givenId(Long id, boolean isDeleted) {
            boolean actual = repository.deleteById(id);

            Assertions.assertThat(actual)
                    .isEqualTo(isDeleted);
        }

        @DisplayName("두번 제거할 경우 실패")
        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 3L})
        void should_returnFalse_when_deleteTwice(Long id) {
            boolean before = repository.deleteById(id);
            boolean after = repository.deleteById(id);

            Assertions.assertThat(before)
                    .isTrue();
            Assertions.assertThat(after)
                    .isFalse();
        }
    }

    void insertReservation(Reservation reservation) {
        jdbcTemplate.update(conn -> reservationStatementCreator.createInsert(conn, reservation));
    }

    void insertTheme(Theme theme) {
        jdbcTemplate.update(conn -> themeStatementCreator.createInsert(conn, theme));
    }
}

