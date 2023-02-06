package nextstep.waiting.repository.jdbc;

import nextstep.member.domain.Member;
import nextstep.schedule.domain.Schedule;
import nextstep.theme.domain.Theme;
import nextstep.waiting.domain.Waiting;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class WaitingResultSetParser {

    public List<Waiting> parseWaitings(ResultSet resultSet) throws SQLException {
        List<Waiting> waitings = new ArrayList<>();
        Waiting waiting = parseWaiting(resultSet);
        while (Objects.nonNull(waiting)) {
            waitings.add(waiting);
            waiting = parseWaiting(resultSet);
        }

        return waitings;
    }

    public Waiting parseWaiting(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        return new Waiting(
                resultSet.getLong("waiting.id"),
                parseSimpleMember(resultSet),
                parseSchedule(resultSet),
                resultSet.getInt("waiting.waitNum")
        );
    }

    private Member parseSimpleMember(ResultSet resultSet) throws SQLException {
        return new Member(
                resultSet.getLong("waiting.member_id"),
                null, null, null, null, null
        );
    }

    private Schedule parseSchedule(ResultSet resultSet) throws SQLException {
        return new Schedule(
                resultSet.getLong("schedule.id"),
                resultSet.getDate("schedule.date").toLocalDate(),
                resultSet.getTime("schedule.time").toLocalTime(),
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
