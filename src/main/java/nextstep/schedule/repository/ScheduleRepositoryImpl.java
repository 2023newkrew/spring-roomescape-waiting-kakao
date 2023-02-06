package nextstep.schedule.repository;

import lombok.RequiredArgsConstructor;
import nextstep.schedule.domain.Schedule;
import nextstep.schedule.repository.jdbc.ScheduleResultSetParser;
import nextstep.schedule.repository.jdbc.ScheduleStatementCreator;
import nextstep.theme.domain.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ScheduleStatementCreator statementCreator;

    private final ScheduleResultSetParser resultSetParser;

    @Override
    public Schedule insert(Schedule reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, reservation),
                keyHolder
        );
        reservation.setId(keyHolder.getKeyAs(Long.class));

        return reservation;
    }

    @Override
    public Schedule getById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseSchedule
        );
    }

    @Override
    public List<Schedule> getAllByThemeAndDate(Theme theme, Date date) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByThemeIdAndDate(connection, theme.getId(), date),
                resultSetParser::parseSchedules
        );
    }

    @Override
    public boolean deleteById(Long id) {
        int deletedRow = jdbcTemplate.update(connection -> statementCreator.createDeleteById(connection, id));

        return deletedRow > 0;
    }
}