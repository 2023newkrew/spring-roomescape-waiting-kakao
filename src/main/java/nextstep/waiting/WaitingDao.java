package nextstep.waiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

@Component
public class WaitingDao {

    private final JdbcTemplate jdbcTemplate;

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
            new Member(
                    resultSet.getLong("member.id"),
                    resultSet.getString("member.username"),
                    resultSet.getString("member.password"),
                    resultSet.getString("member.name"),
                    resultSet.getString("member.phone"),
                    resultSet.getString("member.role")
            ),
            resultSet.getLong("wait_num")
    );

    public Waiting save(Waiting waiting) {
        String sql = "INSERT INTO waiting (schedule_id, member_id) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, waiting.getSchedule().getId());
            ps.setLong(2, waiting.getMember().getId());
            return ps;

        }, keyHolder);

        waiting.setId(keyHolder.getKey().longValue());
        return waiting;
    }
    public Waiting findById(Long id) {
        String sql = "SELECT " +
                "waiting.id, waiting.schedule_id, waiting.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role, " +
                "(SELECT count(*) FROM waiting w WHERE w.schedule_id = schedule.id AND w.id <= waiting.id) as wait_num " +
                "from waiting " +
                "inner join schedule on waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on waiting.member_id = member.id " +
                "where member.id = ?;";

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<Waiting> findAllByMemberId(Long id) {
        String sql = "SELECT " +
                "waiting.id, waiting.schedule_id, waiting.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role, " +
                "(SELECT count(*) FROM waiting w WHERE w.schedule_id = schedule.id AND w.id <= waiting.id) as wait_num " +
                "from waiting " +
                "inner join schedule on waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on waiting.member_id = member.id " +
                "where member.id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM waiting where id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
