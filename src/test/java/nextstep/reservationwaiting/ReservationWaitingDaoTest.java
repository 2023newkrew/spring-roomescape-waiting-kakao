package nextstep.reservationwaiting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.schedule.ScheduleDaoImpl;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import nextstep.theme.ThemeDaoImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ReservationWaitingDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    ReservationWaitingDao reservationWaitingDao;
    ScheduleDao scheduleDao;
    ThemeDao themeDao;
    Long themeId;
    Long scheduleId;

    @BeforeEach
    void setup() {
        createDao();
        dropTables();
        createTable();
        Theme theme = Theme.builder().name("name").desc("desc").price(210000).build();
        themeId = themeDao.save(theme);
        Theme scheduleTheme = Theme.giveId(theme, themeId);
        Schedule schedule = Schedule.builder().theme(scheduleTheme).date(LocalDate.parse("2023-01-26"))
                .time(LocalTime.parse("13:00:00")).build();
        scheduleId = scheduleDao.save(schedule);
    }

    @Test
    @DisplayName("생성 테스트")
    void create() {
        //given
        ReservationWaiting reservationWaiting = createReservationWaitingByWaitNum(1L);

        //when
        Long id = reservationWaitingDao.save(reservationWaiting);

        //then
        Assertions.assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("waitNum이 지정한대로 설정된다.")
    void findWaitNumByScheduleId() {
        IntStream.rangeClosed(1, 10)
                .forEach(i -> {
                    ReservationWaiting reservationWaiting = createReservationWaitingByWaitNum((long) i);
                    reservationWaitingDao.save(reservationWaiting);
                    Assertions.assertThat(reservationWaitingDao.findMaxWaitNumByScheduleId(1L)).isEqualTo((long) i);
                });
    }

    @Test
    @DisplayName("id로 삭제가 가능해야 한다.")
    void deleteById() {
        // given
        ReservationWaiting reservationWaiting = createReservationWaitingByWaitNum(1L);
        Long id = reservationWaitingDao.save(reservationWaiting);
        Integer rows = countRows();
        Assertions.assertThat(rows).isEqualTo(1);

        // when
        reservationWaitingDao.deleteById(id);

        // then
        rows = countRows();
        Assertions.assertThat(rows).isEqualTo(0);
    }

    @Test
    @DisplayName("멤버 아이디로 조회가 가능하다.")
    void findByMemberId() {
        // given
        ReservationWaiting reservationWaiting = createReservationWaitingByWaitNum(1L);
        reservationWaitingDao.save(reservationWaiting);

        // when
        List<ReservationWaiting> reservationWaitings = reservationWaitingDao.findByMemberId(
                reservationWaiting.getMemberId());

        // then
        Assertions.assertThat(countRows()).isEqualTo(1);
        ReservationWaiting foundReservationWaiting = reservationWaitings.get(0);
        Assertions.assertThat(foundReservationWaiting.getMemberId()).isEqualTo(reservationWaiting.getMemberId());
        Assertions.assertThat(foundReservationWaiting.getSchedule().getId())
                .isEqualTo(reservationWaiting.getSchedule().getId());

    }

    @Test
    @DisplayName("id 로 단건 조회")
    void findById() {
        // given
        ReservationWaiting reservationWaiting = createReservationWaitingByWaitNum(1L);
        Long id = reservationWaitingDao.save(reservationWaiting);

        // when
        Optional<ReservationWaiting> foundReservationWaiting = reservationWaitingDao.findById(id);

        // then
        Assertions.assertThat(foundReservationWaiting).isNotEmpty();
        ReservationWaiting result = foundReservationWaiting.get();

        Assertions.assertThat(result.getWaitNum()).isEqualTo(reservationWaiting.getWaitNum());
        Assertions.assertThat(result.getMemberId()).isEqualTo(reservationWaiting.getMemberId());
        Assertions.assertThat(result.getSchedule().getId()).isEqualTo(reservationWaiting.getSchedule().getId());

    }


    private ReservationWaiting createReservationWaitingByWaitNum(Long waitNum) {
        return ReservationWaiting.builder()
                .schedule(
                        Schedule.giveId(Schedule.builder()
                                .theme(Theme.giveId(Theme.builder()
                                        .name("name")
                                        .desc("desc")
                                        .price(210000)
                                        .build(), themeId))
                                .time(LocalTime.parse("13:00:00"))
                                .date(LocalDate.parse("2023-01-26"))
                                .build(), scheduleId))
                .waitNum(waitNum)
                .memberId(1L).build();
    }

    private Integer countRows() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM RESERVATION_WAITING", Integer.class);
    }


    private void createTable() {
        jdbcTemplate.execute("CREATE TABLE reservation_waiting\n" +
                "(\n" +
                "    id          bigint not null auto_increment,\n" +
                "    schedule_id bigint not null,\n" +
                "    member_id   bigint not null,\n" +
                "    wait_num    bigint not null\n" +
                ");" +
                "" +
                "CREATE TABLE schedule\n" +
                "(\n" +
                "    id       bigint not null auto_increment,\n" +
                "    theme_id bigint not null,\n" +
                "    date     date   not null,\n" +
                "    time     time   not null,\n" +
                "    primary key (id)\n" +
                ");" +
                "" +
                "CREATE TABLE theme\n" +
                "(\n" +
                "    id    bigint       not null auto_increment,\n" +
                "    name  varchar(20)  not null,\n" +
                "    desc  varchar(255) not null,\n" +
                "    price int          not null,\n" +
                "    primary key (id)\n" +
                ");" +
                "");
    }

    private void dropTables() {
        jdbcTemplate.execute("DROP TABLE RESERVATION_WAITING IF EXISTS");
        jdbcTemplate.execute("DROP TABLE SCHEDULE IF EXISTS");
        jdbcTemplate.execute("DROP TABLE THEME IF EXISTS");
    }

    private void createDao() {
        reservationWaitingDao = new ReservationWaitingDaoImpl(jdbcTemplate);
        scheduleDao = new ScheduleDaoImpl(jdbcTemplate);
        themeDao = new ThemeDaoImpl(jdbcTemplate);
    }
}