package nextstep.waiting.repository.jdbc;

import nextstep.member.domain.MemberEntity;
import nextstep.schedule.domain.ScheduleEntity;
import nextstep.theme.domain.ThemeEntity;
import nextstep.waiting.domain.WaitingEntity;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class WaitingResultSetParser {

    public List<WaitingEntity> parseWaitings(ResultSet resultSet) throws SQLException {
        List<WaitingEntity> waitings = new ArrayList<>();
        WaitingEntity waiting = parseWaiting(resultSet);
        while (Objects.nonNull(waiting)) {
            waitings.add(waiting);
            waiting = parseWaiting(resultSet);
        }

        return waitings;
    }

    public WaitingEntity parseWaiting(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        return new WaitingEntity(
                resultSet.getLong("waiting.id"),
                parseSimpleMember(resultSet),
                parseSchedule(resultSet),
                resultSet.getInt("waiting.waitNum")
        );
    }

    private MemberEntity parseSimpleMember(ResultSet resultSet) throws SQLException {
        return new MemberEntity(
                resultSet.getLong("waiting.member_id"),
                null, null, null, null, null
        );
    }

    private ScheduleEntity parseSchedule(ResultSet resultSet) throws SQLException {
        return new ScheduleEntity(
                resultSet.getLong("schedule.id"),
                resultSet.getDate("schedule.date").toLocalDate(),
                resultSet.getTime("schedule.time").toLocalTime(),
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
