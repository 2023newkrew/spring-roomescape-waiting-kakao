package app.nextstep.dao;

import app.nextstep.entity.ScheduleEntity;
import app.nextstep.entity.ThemeEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.util.List;

@Component
public class ScheduleDao {
    private JdbcTemplate jdbcTemplate;

    public ScheduleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ScheduleEntity> rowMapper = (resultSet, rowNum) -> new ScheduleEntity(
            resultSet.getLong("schedule.id"),
            new ThemeEntity(
                    resultSet.getLong("theme.id"),
                    resultSet.getString("theme.name"),
                    resultSet.getString("theme.desc"),
                    resultSet.getInt("theme.price")),
            resultSet.getDate("schedule.date"),
            resultSet.getTime("schedule.time")
    );

    public ScheduleEntity findById(Long id) {
        String sql = "SELECT * FROM schedule " +
                "JOIN theme ON schedule.theme_id = theme.id " +
                "WHERE schedule.id = ?;";

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<ScheduleEntity> findByThemeIdAndDate(Long themeId, Date date) {
        String sql = "SELECT * FROM schedule " +
                "JOIN theme ON schedule.theme_id = theme.id " +
                "WHERE schedule.theme_id = ? AND schedule.date = ?;";

        return jdbcTemplate.query(sql, rowMapper, themeId, date);
    }

    public Long save(Long themeId, Date date, Time time) {
        String sql = "INSERT INTO schedule (theme_id, date, time) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        System.out.println("time = " + time);
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, themeId);
            ps.setDate(2, date);
            ps.setTime(3, time);
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM schedule WHERE id = ?;", id);
    }

}
