package nextstep.sales;

import nextstep.domain.member.Member;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.ReservationState;
import nextstep.domain.sales.Sales;
import nextstep.domain.sales.SalesDao;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.theme.Theme;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class SalesDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void save() {
        SalesDao salesDao = new SalesDao(jdbcTemplate);
        Long id = salesDao.save(new Sales(
                new Reservation(
                        1L,
                        new Schedule(
                                new Theme("테마 이름", "테마 설명", 22_000),
                                LocalDate.now(),
                                LocalTime.now()
                        ),
                        new Member(
                                "username",
                                "password",
                                "name",
                                "phone",
                                "USER"
                        ),
                        ReservationState.ACCEPTED
                ), false));
        assertThat(id).isNotNull();
    }


}
