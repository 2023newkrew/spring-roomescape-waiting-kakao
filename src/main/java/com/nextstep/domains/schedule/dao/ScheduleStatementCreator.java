package com.nextstep.domains.schedule.dao;

import com.nextstep.domains.schedule.Schedule;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class ScheduleStatementCreator {

    private static final String
            INSERT_SQL =
            "INSERT INTO schedule (date, time, theme_id) VALUES (?, ?, ?);";

    private static final String
            SELECT_BY_ID_SQL =
            "SELECT schedule.id, schedule.date, schedule.time, theme.id, theme.name, theme.desc, theme.price FROM schedule INNER JOIN theme ON schedule.theme_id = theme.id WHERE schedule.id = ?;";

    private static final String
            SELECT_BY_THEME_ID_AND_DATE_SQL =
            "SELECT schedule.id, schedule.date, schedule.time, theme.id, theme.name, theme.desc, theme.price FROM schedule INNER JOIN theme ON schedule.theme_id = theme.id WHERE theme.id = ? AND schedule.date = ?;";

    private static final String
            DELETE_BY_ID_SQL =
            "DELETE FROM schedule WHERE id = ?;";

    public PreparedStatement createInsert(
            Connection connection, Schedule reservation) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
        setSchedule(ps, reservation);

        return ps;
    }

    private void setSchedule(PreparedStatement ps, Schedule schedule) throws SQLException {
        LocalDate date = schedule.getDate();
        LocalTime time = schedule.getTime();
        ps.setDate(1, Date.valueOf(date));
        ps.setTime(2, Time.valueOf(time));
        ps.setLong(3, schedule.getThemeId());
    }

    public PreparedStatement createSelectById(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL);
        ps.setLong(1, id);

        return ps;
    }

    public PreparedStatement createSelectByThemeIdAndDate(
            Connection connection, Long themeId, Date date) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_THEME_ID_AND_DATE_SQL);
        ps.setLong(1, themeId);
        ps.setDate(2, date);

        return ps;
    }

    public PreparedStatement createDeleteById(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DELETE_BY_ID_SQL);
        ps.setLong(1, id);

        return ps;
    }

}