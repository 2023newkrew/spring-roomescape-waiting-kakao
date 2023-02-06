package nextstep.schedule.repository.jdbc;

import nextstep.schedule.domain.Schedule;
import nextstep.theme.domain.Theme;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ScheduleResultSetParser {

    public List<Schedule> parseSchedules(ResultSet resultSet) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        Schedule schedule = parseSchedule(resultSet);
        while (Objects.nonNull(schedule)) {
            schedules.add(schedule);
            schedule = parseSchedule(resultSet);
        }

        return schedules;
    }

    public Schedule parseSchedule(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Date date = resultSet.getDate("schedule.date");
        Time time = resultSet.getTime("schedule.time");

        return new Schedule(
                resultSet.getLong("schedule.id"),
                date.toLocalDate(),
                time.toLocalTime(),
                parseTheme(resultSet)
        );
    }

    private Theme parseTheme(ResultSet resultSet) throws SQLException {
        return new Theme(
                resultSet.getLong("theme.id"),
                resultSet.getString("theme.name"),
                resultSet.getString("theme.desc"),
                resultSet.getInt("theme.price")
        );
    }
}
