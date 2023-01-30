package nextstep.reservationwaiting;

import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@JdbcTest
class ReservationWaitingDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    ReservationWaitingDao reservationWaitingDao;
    ScheduleDao scheduleDao;
    ThemeDao themeDao;
    Schedule schedule;

    @BeforeEach
    void setup() {
        createDao();
        dropTables();
        createTable();
        Long themeId = themeDao.save(new Theme("name", "desc", 210000));
        Theme theme = new Theme(themeId, "name", "desc", 210000);
        Long scheduleId = scheduleDao.save(new Schedule(theme, LocalDate.parse("2023-01-26"), LocalTime.parse("13:00:00")));
        schedule = new Schedule(scheduleId, theme, LocalDate.parse("2023-01-26"), LocalTime.parse("13:00:00"));
    }

    @Test
    @DisplayName("예약 대기가 db 에 잘 반영된다.")
    void create() {
        //given
        ReservationWaiting reservationWaiting = createReservationWaitingByWaitNum(1L);

        //when
        Long id = reservationWaitingDao.save(reservationWaiting);

        //then
        Assertions.assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("waitNum 이 예약 대기를 신청하는 순서대로 1씩 증가한다")
    void findWaitNumByScheduleId() {
        IntStream.rangeClosed(1, 10)
                .forEach(i -> {
                    ReservationWaiting reservationWaiting = createReservationWaitingByWaitNum((long) i);
                    reservationWaitingDao.save(reservationWaiting);
                    Assertions.assertThat(reservationWaitingDao.findMaxWaitNumByScheduleId(1L)).isEqualTo(i);
                });
    }

    @Test
    @DisplayName("id 로 삭제가 가능해아 한다.")
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
    @DisplayName("멤버 아이디로 조회가 가능해야 한다.")
    void findByMemberId() {
        // given
        ReservationWaiting reservationWaiting = createReservationWaitingByWaitNum(1L);
        reservationWaitingDao.save(reservationWaiting);

        // when
        List<ReservationWaiting> reservationWaitings = reservationWaitingDao.findByMemberId(reservationWaiting.getMemberId());

        // then
        Assertions.assertThat(reservationWaitings.size()).isEqualTo(1);
        ReservationWaiting foundReservationWaiting = reservationWaitings.get(0);
        Assertions.assertThat(foundReservationWaiting.getMemberId()).isEqualTo(reservationWaiting.getMemberId());
        Assertions.assertThat(foundReservationWaiting.getSchedule().getId()).isEqualTo(reservationWaiting.getSchedule().getId());
    }

    @Test
    @DisplayName("스케줄 id로 조회가 가능해야 한다.")
    void findEarliestOneByScheduleId() {
        // given
        ReservationWaiting reservationWaiting = createReservationWaitingByWaitNum(1L);
        reservationWaitingDao.save(reservationWaiting);

        // when
        ReservationWaiting foundReservationWaiting = reservationWaitingDao.findEarliestOneByScheduleId(reservationWaiting.getSchedule().getId());

        // then
        Assertions.assertThat(foundReservationWaiting).isNotNull();
    }

    @Test
    @DisplayName("스케줄 id로 조회시 waitNum 이 가장 낮은 entity 가 조회되어야 한다.")
    void findEarliestOneByScheduleId2() {
        // given
        ReservationWaiting reservationWaiting = createReservationWaitingByWaitNum(1L);
        reservationWaitingDao.save(reservationWaiting);
        reservationWaitingDao.save(reservationWaiting);

        // when
        ReservationWaiting foundReservationWaiting = reservationWaitingDao.findEarliestOneByScheduleId(reservationWaiting.getSchedule().getId());

        // then
        Assertions.assertThat(foundReservationWaiting.getWaitNum()).isEqualTo(1L);
    }

    private ReservationWaiting createReservationWaitingByWaitNum(Long waitNum) {
        return new ReservationWaiting(schedule, 1L, waitNum);
    }

    private Integer countRows() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM RESERVATION_WAITING", Integer.class);
    }

    private void createTable() {
        jdbcTemplate.execute(
                "CREATE TABLE reservation_waiting" +
                "(" +
                "    id          bigint not null auto_increment," +
                "    schedule_id bigint not null," +
                "    member_id   bigint not null," +
                "    wait_num    bigint not null" +
                ");" +
                "" +
                "CREATE TABLE schedule" +
                "(" +
                "    id       bigint not null auto_increment," +
                "    theme_id bigint not null," +
                "    date     date   not null," +
                "    time     time   not null," +
                "    primary key (id)" +
                ");" +
                "" +
                "CREATE TABLE theme" +
                "(" +
                "    id    bigint       not null auto_increment," +
                "    name  varchar(20)  not null," +
                "    desc  varchar(255) not null," +
                "    price int          not null," +
                "    primary key (id)" +
                ");"
        );
    }

    private void dropTables() {
        Arrays.asList("RESERVATION_WAITING", "SCHEDULE", "THEME").forEach(table -> {
            jdbcTemplate.execute("DROP TABLE " + table + " IF EXISTS");
        });
    }

    private void createDao() {
        reservationWaitingDao = new ReservationWaitingDao(jdbcTemplate);
        scheduleDao = new ScheduleDao(jdbcTemplate);
        themeDao = new ThemeDao(jdbcTemplate);
    }
}