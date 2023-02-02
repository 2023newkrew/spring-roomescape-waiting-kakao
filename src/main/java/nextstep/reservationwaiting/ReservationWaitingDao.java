package nextstep.reservationwaiting;

import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
public class ReservationWaitingDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationWaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationWaiting> rowMapper = (resultSet, rowNum) -> new ReservationWaiting(
            resultSet.getLong("reservation_waiting.id"),
            new Schedule(
                    resultSet.getLong("schedule.id"),
                    new Theme(
                            resultSet.getLong("theme.id"),
                            resultSet.getString("theme.name"),
                            resultSet.getString("theme.desc"),
                            resultSet.getInt("theme.price")
                    ),
                    resultSet.getDate("schedule.date").toLocalDate(),
                    resultSet.getTime("schedule.time").toLocalTime()
            ),
            resultSet.getLong("reservation_waiting.member_id"),
            resultSet.getLong("reservation_waiting.wait_num")
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation_waiting (schedule_id, member_id, wait_num) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setLong(2, reservationWaiting.getMemberId());
            ps.setLong(3, reservationWaiting.getWaitNum());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<ReservationWaiting> findByMemberId(Long memberId) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.member_id, reservation_waiting.wait_num, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "FROM reservation_waiting " +
                "INNER JOIN schedule ON reservation_waiting.schedule_id = schedule.id " +
                "INNER JOIN theme ON schedule.theme_id = theme.id " +
                "WHERE reservation_waiting.member_id = ?;";
        try {
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch (Exception e) {
            return null;
        }
    }

    public ReservationWaiting findEarliestOneByScheduleId(Long scheduleId) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.member_id, reservation_waiting.wait_num, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price " +
                "FROM reservation_waiting " +
                "INNER JOIN schedule ON reservation_waiting.schedule_id = schedule.id " +
                "INNER JOIN theme ON schedule.theme_id = theme.id " +
                "WHERE reservation_waiting.schedule_id = ? " +
                "ORDER BY reservation_waiting.wait_num ASC " +
                "LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, scheduleId);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existById(Long id, Long memberId) {
        String sql = "SELECT " +
                "1 " +
                "FROM reservation_waiting " +
                "WHERE member_id = ? AND id = ? " +
                "LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, memberId, id) == 1;
        } catch (Exception e) {
            return false;
        }
    }

    public Long findMaxWaitNumByScheduleId(Long scheduleId) {
        String sql = "SELECT " +
                "max(reservation_waiting.wait_num) " +
                "FROM reservation_waiting " +
                "WHERE reservation_waiting.schedule_id = ?;";
        try {
            return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, Long.class, scheduleId));
        } catch (Exception e) {
            return 0L;
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_waiting WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }
}

