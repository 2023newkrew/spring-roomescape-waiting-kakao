package nextstep.reservationwaiting;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class ReservationWaitingDao {
    public final JdbcTemplate jdbcTemplate;

    public ReservationWaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationWaiting> rowMapper = (resultSet, rowNum) -> new ReservationWaiting(
            resultSet.getLong("reservationWaiting.id"),
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
            resultSet.getLong("reservationWaiting.member_id"),
            resultSet.getLong("reservationWaiting.priority")
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservationWaiting (schedule_id, member_id, priority) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setLong(2, reservationWaiting.getMemberId());
            ps.setLong(3, reservationWaiting.getPriority());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Long getMaxPriority(Schedule schedule) {
        String sql = "SELECT MAX(priority) FROM reservationWaiting WHERE schedule_id = ?;";
        try {
            return jdbcTemplate.query(sql,
                    rs -> {
                        if (rs.next()) {
                            return rs.getLong(1);
                        } else {
                            throw new SQLException();
                        }
                    });
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReservationWaiting> findAllByMemberId(Long memberId) {
        String sql = "SELECT " +
                "reservationWaiting.id, "+
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "reservationWaiting.member_id, reservationWaiting.priority" +
                "from reservationWaiting " +
                "inner join schedule on reservationWaiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "where reservationWaiting.member_id = ?;";
        try {
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch (Exception e) {
            return null;
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservationWaiting where id = ?;";
        return jdbcTemplate.update(sql, id);
    }
}
