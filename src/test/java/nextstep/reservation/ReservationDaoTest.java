package nextstep.reservation;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ReservationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationDao reservationDao;
    private ScheduleDao scheduleDao;
    private ThemeDao themeDao;
    private MemberDao memberDao;

    @BeforeEach
    void setupDao() {
        reservationDao = new ReservationDao(jdbcTemplate);
        scheduleDao = new ScheduleDao(jdbcTemplate);
        themeDao = new ThemeDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void getPriority() {
        Theme theme = generateTheme();
        Schedule schedule = generateSchedule(theme);
        Member member = generateMember();
        Reservation willDeleteReservation = generateReservation(schedule, member);
        Reservation firstReservation = generateReservation(schedule, member);
        Reservation secondReservation = generateReservation(schedule, member);
        Reservation thirdReservation = generateReservation(schedule, member);
        reservationDao.deleteById(willDeleteReservation.getId());
        Reservation fourthReservation = generateReservation(schedule, member);
        Reservation fifthReservation = generateReservation(schedule, member);
        Reservation sixthReservation = generateReservation(schedule, member);

        assertThat(reservationDao.getPriority(schedule.getId(), firstReservation.getWaitTicketNumber())).isEqualTo(0L);
        assertThat(reservationDao.getPriority(schedule.getId(), secondReservation.getWaitTicketNumber())).isEqualTo(1L);
        assertThat(reservationDao.getPriority(schedule.getId(), thirdReservation.getWaitTicketNumber())).isEqualTo(2L);
        assertThat(reservationDao.getPriority(schedule.getId(), fourthReservation.getWaitTicketNumber())).isEqualTo(3L);
        assertThat(reservationDao.getPriority(schedule.getId(), fifthReservation.getWaitTicketNumber())).isEqualTo(4L);
        assertThat(reservationDao.getPriority(schedule.getId(), sixthReservation.getWaitTicketNumber())).isEqualTo(5L);
    }

    private Reservation generateReservation(Schedule schedule, Member member) {
        Long firstReservationId = reservationDao.save(new Reservation(schedule, member));
        return reservationDao.findById(firstReservationId).orElseThrow();
    }

    private Theme generateTheme() {
        Long themeId = themeDao.save(new Theme("testName", "testDesc", 22000));
        return themeDao.findById(themeId).orElseThrow();
    }

    private Schedule generateSchedule(Theme theme) {
        Long scheduleId = scheduleDao.save(new Schedule(theme, LocalDate.of(2023, 2, 1), LocalTime.of(13, 30)));
        return scheduleDao.findById(scheduleId).orElseThrow();
    }

    private Member generateMember() {
        Long memberId = memberDao.save(new Member("username", "password", "name", "010-1234-5678", "USER"));
        return memberDao.findById(memberId).orElseThrow();
    }
}
