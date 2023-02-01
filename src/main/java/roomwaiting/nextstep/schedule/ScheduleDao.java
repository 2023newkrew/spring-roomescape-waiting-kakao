package roomwaiting.nextstep.schedule;

import roomwaiting.nextstep.dbmapper.DatabaseMapper;
import roomwaiting.nextstep.dbmapper.H2Mapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ScheduleDao {
    private final JdbcTemplate jdbcTemplate;
    private final DatabaseMapper databaseMapper;

    public ScheduleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.databaseMapper = new H2Mapper();
    }

    public Long save(Schedule schedule) {
        String sql = "INSERT INTO schedule (theme_id, date, time) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, schedule.getTheme().getId());
            ps.setDate(2, Date.valueOf(schedule.getDate()));
            ps.setTime(3, Time.valueOf(schedule.getTime()));
            return ps;

        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<Schedule> findById(Long id) {
        String sql = "SELECT schedule.id, schedule.theme_id, schedule.date, schedule.time, theme.id, theme.name, theme.description, theme.price " +
                "from schedule " +
                "inner join theme on schedule.theme_id = theme.id " +
                "where schedule.id = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, databaseMapper.scheduleRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Schedule> findByThemeIdAndDate(Long themeId, String date) {
        String sql = "SELECT schedule.id, schedule.theme_id, schedule.date, schedule.time, theme.id, theme.name, theme.description, theme.price " +
                "from schedule " +
                "inner join theme on schedule.theme_id = theme.id " +
                "where schedule.theme_id = ? and schedule.date = ?;";
        return jdbcTemplate.query(sql, databaseMapper.scheduleRowMapper(), themeId, Date.valueOf(LocalDate.parse(date)));
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM schedule where id = ?;", id);
    }

    public Boolean isExistsByTimeAndDate(String time, String date){
        String sql = "SELECT EXISTS (SELECT 1 FROM schedule WHERE time = ? and date = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, time, date);
    }

    public Boolean isExistsByThemeId(Long themeId){
        String sql = "SELECT EXISTS(SELECT 1 FROM schedule WHERE theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

}
