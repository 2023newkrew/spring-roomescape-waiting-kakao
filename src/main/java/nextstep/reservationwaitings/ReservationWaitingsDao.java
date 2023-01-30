package nextstep.reservationwaitings;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class ReservationWaitingsDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationWaitingsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long findWaitNumByScheduleId(Long scheduleId) {
        String sql = "SELECT max(wait_num) as max_wait_num FROM reservation_waitings WHERE schedule_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("max_wait_num"), scheduleId);
    }

    public long create(ReservationWaitings reservationWaitings) {
        String sql = "INSERT INTO reservation_waitings (schedule_id, member_id, wait_num) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaitings.getSchedule().getId());
            ps.setLong(2, reservationWaitings.getMember().getId());
            ps.setLong(3, reservationWaitings.getWaitNum());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
