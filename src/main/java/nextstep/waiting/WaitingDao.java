package nextstep.waiting;

import auth.Role;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class WaitingDao {

    public final JdbcTemplate jdbcTemplate;


    public WaitingDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<Waiting> rowMapper = (resultSet, rowNum) -> Waiting.builder()
            .id(resultSet.getLong("waiting.id"))
            .schedule(Schedule.builder()
                    .id(resultSet.getLong("schedule.id"))
                    .theme(Theme.builder()
                            .id(resultSet.getLong("theme.id"))
                            .name(resultSet.getString("theme.name"))
                            .desc(resultSet.getString("theme.desc"))
                            .price(resultSet.getInt("theme.price"))
                            .build())
                    .date(resultSet.getDate("schedule.date").toLocalDate())
                    .time(resultSet.getTime("schedule.time").toLocalTime())
                    .build())
            .member(Member.builder()
                    .id(resultSet.getLong("member.id"))
                    .username(resultSet.getString("member.username"))
                    .password(resultSet.getString("member.password"))
                    .name(resultSet.getString("member.name"))
                    .phone(resultSet.getString("member.phone"))
                    .role(Role.valueOf(resultSet.getString("member.role")))
                    .build())
            .build();

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

    public List<Waiting> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = "SELECT " +
                "waiting.id, waiting.schedule_id, waiting.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from waiting " +
                "inner join schedule on waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on waiting.member_id = member.id " +
                "where theme.id = ? and schedule.date = ?;";

        return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(date));
    }

    public Optional<Waiting> findById(Long id) {
        String sql = "SELECT " +
                "waiting.id, waiting.schedule_id, waiting.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from waiting " +
                "inner join schedule on waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on waiting.member_id = member.id " +
                "where waiting.id = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Waiting> findByScheduleId(Long id) {
        String sql = "SELECT " +
                "waiting.id, waiting.schedule_id, waiting.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from waiting " +
                "inner join schedule on waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on waiting.member_id = member.id " +
                "where schedule.id = ?;";

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

    public List<Waiting> findByMemberId(Long id) {
        String sql = "SELECT " +
                "waiting.id, waiting.schedule_id, waiting.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
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

    public int countWaitingNumber(Waiting waiting) {
        String sql = "SELECT COUNT(*) FROM waiting WHERE schedule.id = ? AND id <= ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, waiting.getSchedule().getId(), waiting.getId());
    }
}
