package nextstep.reservation_waiting;

import nextstep.reservation.ReservationDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"classpath:schema.sql", "classpath:reservation_data.sql"})
public class ReservationWaitingDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationDao reservationDao;

    @BeforeEach
    void setUp() {
        reservationDao = new ReservationDao(jdbcTemplate);
    }

    @DisplayName("")
    @Test
    void testFindAllByMember() {
        assertThat(reservationDao.findAllWaitingByMemberId(1L).size()).isEqualTo(1);
        assertThat(reservationDao.findAllWaitingByMemberId(2L).size()).isEqualTo(2);
        assertThat(reservationDao.findAllWaitingByMemberId(3L).size()).isEqualTo(3);
    }
}
