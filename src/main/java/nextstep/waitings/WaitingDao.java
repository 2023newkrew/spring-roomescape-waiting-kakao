package nextstep.waitings;

import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;


@Repository
public class WaitingDao {
    public final JdbcTemplate jdbcTemplate;

    public WaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Waiting> rowMapper = (resultSet, rowNum) -> new Waiting(
            resultSet.getLong("waiting.id"),
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
            resultSet.getLong("waiting.member_id")
    );

    public long save(final Long scheduleId, final Long memberId) {
        String sql = "INSERT INTO waiting (schedule_id, member_id) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, scheduleId);
            ps.setLong(2, memberId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Long countByScheduleId(final Long scheduleId) {
        String sql = "SELECT COUNT(*) FROM waitings WHERE schedule_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, scheduleId);
    }
}
