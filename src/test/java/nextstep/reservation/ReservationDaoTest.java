package nextstep.reservation;

import nextstep.member.MemberDao;
import nextstep.schedule.ScheduleDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
                .schedule(scheduleDao.findById(4L).get())
                .member(memberDao.findById(1L).get())
                .build();
        Long id = reservationDao.save(newReservation);
        assertThat(id).isInstanceOf(Long.class);
    }


    @DisplayName("테마와 날짜로 예약들을 불러올 수 있다.")
    @Test
    void findAllByThemeIdAndDate() {
        List<Reservation> reservations = reservationDao.findAllByThemeIdAndDate(1L, "2023-01-30");
        assertThat(reservations.size()).isEqualTo(3);
    }

    @DisplayName("아이디로 예약을 불러올 수 있다.")
    @Test
    void findById() {
        assertThat(reservationDao.findById(1L).get()).isInstanceOf(Reservation.class);
    }

    @DisplayName("스케줄로 예약을 불러올 수 있다.")
    @Test
    void findByScheduleId() {
        assertThat(reservationDao.findByScheduleId(1L).get()).isInstanceOf(Reservation.class);
    }

    @DisplayName("아이디로 예약을 삭제할 수 있다.")
    @Test
    void deleteById() {
        reservationDao.deleteById(1L);
        assertDoesNotThrow(() -> reservationDao.findById(1L));
    }


    @DisplayName("모든 예약을 불러올 수 있다.")
    @Test
    void findAllByMemberId() {
        assertThat(reservationDao.findAllByMemberId(1L).size()).isEqualTo(2);

        assertThat(reservationDao.findAllByMemberId(2L).size()).isEqualTo(1);

        assertThat(reservationDao.findAllByMemberId(3L).size()).isEqualTo(0);
    }

    @DisplayName("예약 상태를 변경할 수 있다.")
    @Test
    void updateState() {
        reservationDao.updateState(1L, ReservationState.APPROVE);
        assertThat(reservationDao.findById(1L).get().getState()).isEqualTo(ReservationState.APPROVE);
        reservationDao.updateState(1L, ReservationState.CANCEL);
        assertThat(reservationDao.findById(1L).get().getState()).isEqualTo(ReservationState.CANCEL);
        reservationDao.updateState(1L, ReservationState.CANCEL_WAIT);
        assertThat(reservationDao.findById(1L).get().getState()).isEqualTo(ReservationState.CANCEL_WAIT);
        reservationDao.updateState(1L, ReservationState.REJECT);
        assertThat(reservationDao.findById(1L).get().getState()).isEqualTo(ReservationState.REJECT);
        reservationDao.updateState(1L, ReservationState.UN_APPROVE);
        assertThat(reservationDao.findById(1L).get().getState()).isEqualTo(ReservationState.UN_APPROVE);
    }
}
