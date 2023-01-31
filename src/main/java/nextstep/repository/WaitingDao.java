package nextstep.repository;

import lombok.RequiredArgsConstructor;
import nextstep.domain.persist.Member;
import nextstep.domain.persist.Schedule;
import nextstep.domain.persist.Theme;
import nextstep.domain.persist.Waiting;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WaitingDao {
    public final JdbcTemplate jdbcTemplate;

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
            new Member(
                    resultSet.getLong("member.id"),
                    resultSet.getString("member.username"),
                    resultSet.getString("member.password"),
                    resultSet.getString("member.name"),
                    resultSet.getString("member.phone"),
                    resultSet.getString("member.role")
            )
    );

    public Long save(Waiting waiting) {
        String sql = "INSERT INTO waiting (schedule_id, member_id) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, waiting.getSchedule().getId());
            ps.setLong(2, waiting.getMember().getId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Long getPriority(Long scheduleId, Long waitingId) {
        String sql = "SELECT COUNT(waiting.id)" +
                "FROM waiting " +
                "WHERE waiting.schedule_id = ? AND waiting.id <= ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, scheduleId, waitingId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Waiting> findAll(Long id) {
        String sql = "SELECT " +
                "waiting.id, waiting.schedule_id, waiting.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "FROM waiting " +
                "inner join schedule on waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on waiting.member_id = member.id " +
                "WHERE waiting.member_id = ?;";
        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM waiting WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Waiting findById(Long id) {
        String sql = "SELECT * FROM waiting WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
}
