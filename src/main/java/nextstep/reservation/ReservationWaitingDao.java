package nextstep.reservation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class ReservationWaitingDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationWaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation_waiting (member_id, schedule_id, waitNum) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getMember().getId());
            ps.setLong(2, reservationWaiting.getSchedule().getId());
            ps.setInt(3, reservationWaiting.getWaitNum());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int findMaxWaitNum(Long scheduleId) {
        String sql = "SELECT MAX(wait_num) FROM reservation_waiting WHERE schedule_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, scheduleId);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

}
