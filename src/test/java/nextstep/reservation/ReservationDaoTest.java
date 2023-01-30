package nextstep.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("classpath:reservation_data.sql")
public class ReservationDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationDao reservationDao;

    @BeforeEach
    void setUp() {
        reservationDao = new ReservationDao(jdbcTemplate);
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
