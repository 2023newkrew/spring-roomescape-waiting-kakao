package nextstep.reservation;

import java.util.Optional;
import nextstep.reservation_waiting.ReservationWaiting;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import static nextstep.utils.RowMapperUtil.reservationRowMapper;
import static nextstep.utils.RowMapperUtil.reservationStatusHistoryMapper;
import static nextstep.utils.RowMapperUtil.reservationWaitingRowMapper;

@Component
public class ReservationDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, member_id) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setLong(2, reservation.getMember().getId());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = """
                    SELECT *
                    FROM RESERVATION, 
                        (SELECT R.id
                                RESERVATION_ID,
                                ROW_NUMBER() OVER (PARTITION BY R.SCHEDULE_ID ORDER BY R.CREATED_DATETIME) WAIT_NUM
                         FROM RESERVATION R)
                    JOIN SCHEDULE ON RESERVATION.SCHEDULE_ID = SCHEDULE.ID
                    JOIN THEME ON SCHEDULE.THEME_ID = THEME.ID
                    JOIN MEMBER ON RESERVATION.MEMBER_ID = MEMBER.ID
                    where RESERVATION.id = RESERVATION_ID
                        and WAIT_NUM = 1
                        and THEME.id = (?)
                        and SCHEDULE.DATE = (?)
                    """ ;
        return jdbcTemplate.query(sql, reservationRowMapper, themeId, Date.valueOf(date));
    }

    public Optional<Reservation> findById(Long id) {
        String sql ="""
                SELECT *
                FROM reservation
                JOIN schedule ON reservation.schedule_id = schedule.id
                JOIN theme ON schedule.theme_id = theme.id
                JOIN member ON reservation.member_id = member.id
                WHERE reservation.id = ?
                """;

        return jdbcTemplate.query(sql, reservationRowMapper, id).stream().findAny();
    }

    public Optional<Reservation> findByScheduleId(Long id) {
        String sql = """
                    SELECT *
                    FROM RESERVATION, 
                        (SELECT R.id
                                RESERVATION_ID,
                                ROW_NUMBER() OVER (PARTITION BY R.SCHEDULE_ID ORDER BY R.CREATED_DATETIME) WAIT_NUM
                         FROM RESERVATION R)
                    JOIN SCHEDULE ON RESERVATION.SCHEDULE_ID = SCHEDULE.ID
                    JOIN THEME ON SCHEDULE.THEME_ID = THEME.ID
                    JOIN MEMBER ON RESERVATION.MEMBER_ID = MEMBER.ID
                    WHERE RESERVATION.id = RESERVATION_ID
                        AND WAIT_NUM = 1
                        AND SCHEDULE_ID = (?)
                    """ ;
        return jdbcTemplate.query(sql, reservationRowMapper, id).stream().findAny();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public List<Reservation> findAllByMemberId(Long memberId) {
        String sql = """
                    SELECT *
                    FROM RESERVATION, 
                        (SELECT R.id
                                RESERVATION_ID,
                                ROW_NUMBER() OVER (PARTITION BY R.SCHEDULE_ID ORDER BY R.CREATED_DATETIME) WAIT_NUM
                         FROM RESERVATION R)
                    JOIN SCHEDULE ON RESERVATION.SCHEDULE_ID = SCHEDULE.ID
                    JOIN THEME ON SCHEDULE.THEME_ID = THEME.ID
                    JOIN MEMBER ON RESERVATION.MEMBER_ID = MEMBER.ID
                    WHERE RESERVATION.id = RESERVATION_ID
                        AND WAIT_NUM = 1
                        AND MEMBER_ID = (?)
                    """ ;
        return jdbcTemplate.query(sql, reservationRowMapper, memberId);
    }

    public List<ReservationWaiting> findAllWaitingByMemberId(Long memberId) {
        String sql = """
                    SELECT *
                    FROM RESERVATION, 
                        (SELECT R.id
                                RESERVATION_ID,
                                ROW_NUMBER() OVER (PARTITION BY R.SCHEDULE_ID ORDER BY R.CREATED_DATETIME) WAIT_NUM
                         FROM RESERVATION R)
                    JOIN SCHEDULE ON RESERVATION.SCHEDULE_ID = SCHEDULE.ID
                    JOIN THEME ON SCHEDULE.THEME_ID = THEME.ID
                    JOIN MEMBER ON RESERVATION.MEMBER_ID = MEMBER.ID
                    WHERE RESERVATION.id = RESERVATION_ID
                        AND WAIT_NUM > 1
                        AND MEMBER_ID = (?)
                    """ ;
        return jdbcTemplate.query(sql, reservationWaitingRowMapper, memberId);
    }

    public void updateStatus(Long id, ReservationStatus status) {
        String sql = "update reservation set status = (?) where RESERVATION.id = (?)";
        jdbcTemplate.update(sql, status.name(), id);
    }

    public void saveStatusHistory(ReservationStatusHistory history) {
        String sql = "INSERT INTO reservation_status_history (reservation_id, before_status, after_status ) VALUES (?, ?, ?);";
        jdbcTemplate.update(sql, history.getReservationId(), history.getBeforeStatus().name(), history.getAfterStatus().name());
    }

    public Optional<ReservationStatusHistory> findStatusHistoryById(Long id) {
        String sql = "select * from reservation_status_history where id = (?)";
        return jdbcTemplate.query(sql, reservationStatusHistoryMapper, id).stream().findFirst();
    }
}
