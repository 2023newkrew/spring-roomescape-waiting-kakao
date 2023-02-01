package nextstep.schedule;

import static nextstep.schedule.ScheduleJdbcSql.DELETE_BY_ID;
import static nextstep.schedule.ScheduleJdbcSql.INSERT_INTO_STATEMENT;
import static nextstep.schedule.ScheduleJdbcSql.SELECT_BY_ID_STATEMENT;
import static nextstep.schedule.ScheduleJdbcSql.SELECT_BY_THEME_ID_AND_DATE_STATEMENT;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Schedule> rowMapper = (resultSet, rowNum) ->
            Schedule.giveId(Schedule.builder()
                            .theme(Theme.giveId(Theme.builder()
                                    .name(resultSet.getString("theme.name"))
                                    .desc(resultSet.getString("theme.desc"))
                                    .price(resultSet.getInt("theme.price"))
                                    .build(), resultSet.getLong("theme.id"))
                            )
                            .time(resultSet.getTime("schedule.time").toLocalTime())
                            .date(resultSet.getDate("schedule.date").toLocalDate())
                            .build()
                    , resultSet.getLong("schedule.id"));

    @Override
    public Long save(Schedule schedule) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_INTO_STATEMENT, new String[]{"id"});
            ps.setLong(1, schedule.getTheme().getId());
            ps.setDate(2, Date.valueOf(schedule.getDate()));
            ps.setTime(3, Time.valueOf(schedule.getTime()));
            return ps;

        }, keyHolder);

        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public Schedule findById(Long id) {
        return jdbcTemplate.query(SELECT_BY_ID_STATEMENT, rowMapper, id).stream().findFirst().orElse(null);
    }

    @Override
    public List<Schedule> findByThemeIdAndDate(Long themeId, String date) {

        return jdbcTemplate.query(
                SELECT_BY_THEME_ID_AND_DATE_STATEMENT, rowMapper, themeId, Date.valueOf(LocalDate.parse(date)));
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

}
