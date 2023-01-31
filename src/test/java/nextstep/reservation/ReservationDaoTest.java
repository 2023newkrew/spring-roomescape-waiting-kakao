package nextstep.reservation;

import nextstep.member.MemberDao;
import nextstep.schedule.ScheduleDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"classpath:schema.sql", "classpath:reservation_data.sql"})
public class ReservationDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationDao reservationDao;
    private ScheduleDao scheduleDao;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        reservationDao = new ReservationDao(jdbcTemplate);
        scheduleDao = new ScheduleDao(jdbcTemplate);
        memberDao = new MemberDao(jdbcTemplate);
    }


    @DisplayName("예약 저장 테스트")
    @Test
    void save() {
        Reservation newReservation = Reservation.builder()
                .schedule(scheduleDao.findById(4L))
                .member(memberDao.findById(1L))
                .build();
        Long id = reservationDao.save(newReservation);
        assertThat(id).isInstanceOf(Long.class);
    }

    @Test
    void findAllByMemberId() {
        List<Reservation> reservations = reservationDao.findAllByMemberId(1L);
        assertThat(reservations.size()).isEqualTo(2);

        reservations = reservationDao.findAllByMemberId(2L);
        assertThat(reservations.size()).isEqualTo(1);

        reservations = reservationDao.findAllByMemberId(3L);
        assertThat(reservations.size()).isEqualTo(0);
    }
}
