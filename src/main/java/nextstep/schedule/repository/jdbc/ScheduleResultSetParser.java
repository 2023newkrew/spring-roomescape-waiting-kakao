package nextstep.schedule.repository.jdbc;

import nextstep.schedule.domain.ScheduleEntity;
import nextstep.theme.domain.ThemeEntity;
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

    public List<ScheduleEntity> parseSchedules(ResultSet resultSet) throws SQLException {
        List<ScheduleEntity> schedules = new ArrayList<>();
        ScheduleEntity schedule = parseSchedule(resultSet);
        while (Objects.nonNull(schedule)) {
            schedules.add(schedule);
            schedule = parseSchedule(resultSet);
        }

        return schedules;
    }

    public ScheduleEntity parseSchedule(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Date date = resultSet.getDate("schedule.date");
        Time time = resultSet.getTime("schedule.time");

        return new ScheduleEntity(
                resultSet.getLong("schedule.id"),
                date.toLocalDate(),
                time.toLocalTime(),
                parseTheme(resultSet)
        );
    }

    private ThemeEntity parseTheme(ResultSet resultSet) throws SQLException {
        return new ThemeEntity(
                resultSet.getLong("theme.id"),
                resultSet.getString("theme.name"),
                resultSet.getString("theme.desc"),
                resultSet.getInt("theme.price")
        );
    }
}
