package com.nextstep.domains.schedule;

import com.nextstep.domains.schedule.dao.ScheduleResultSetParser;
import lombok.RequiredArgsConstructor;
import com.nextstep.domains.schedule.dao.ScheduleStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    private final ScheduleStatementCreator statementCreator;

    private final ScheduleResultSetParser resultSetParser;

    public Schedule insert(Schedule reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, reservation),
                keyHolder
        );
        reservation.setId(keyHolder.getKeyAs(Long.class));

        return reservation;
    }

    public Schedule getById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseSchedule
        );
    }

    public List<Schedule> getByThemeIdAndDate(Long themeId, Date date) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByThemeIdAndDate(connection, themeId, date),
                resultSetParser::parseSchedules
        );
    }

    public boolean deleteById(Long id) {
        int deletedRow = jdbcTemplate.update(connection -> statementCreator.createDeleteById(connection, id));

        return deletedRow > 0;
    }
}