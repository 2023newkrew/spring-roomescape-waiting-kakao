package nextstep.waiting.repository.jdbc;

import nextstep.waiting.domain.Waiting;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class WaitingStatementCreator {

    private static final String INSERT_SQL = "INSERT INTO waiting (member_id, schedule_id) VALUES (?, ?);";

    private static final String SELECT_BY_ID_SQL = "SELECT * \n" +
            "FROM \n" +
            "    (SELECT \n" +
            "         id, \n" +
            "         member_id, \n" +
            "         schedule_id,\n" +
            "         RANK() OVER (PARTITION BY schedule_id ORDER BY id ASC) AS waitNum \n" +
            "     FROM waiting AS W) AS waiting \n" +
            "INNER JOIN schedule ON waiting.schedule_id = schedule.id \n" +
            "INNER JOIN theme ON schedule.theme_id = theme.id \n" +
            "WHERE waiting.id = ?";

    private static final String SELECT_BY_SCHEDULE_ID_SQL = "SELECT * \n" +
            "FROM \n" +
            "    (SELECT \n" +
            "         id, \n" +
            "         member_id, \n" +
            "         schedule_id,\n" +
            "         RANK() OVER (PARTITION BY schedule_id ORDER BY id ASC) AS waitNum \n" +
            "     FROM waiting AS W) AS waiting \n" +
            "INNER JOIN schedule ON waiting.schedule_id = schedule.id \n" +
            "INNER JOIN theme ON schedule.theme_id = theme.id \n" +
            "WHERE schedule.id = ? " +
            "ORDER BY waiting.id ASC " +
            "LIMIT 1";

    private static final String SELECT_BY_MEMBER_ID_SQL = "SELECT * \n" +
            "FROM \n" +
            "    (SELECT \n" +
            "         id, \n" +
            "         member_id, \n" +
            "         schedule_id,\n" +
            "         RANK() OVER (PARTITION BY schedule_id ORDER BY id ASC) AS waitNum \n" +
            "     FROM waiting AS W) AS waiting \n" +
            "INNER JOIN schedule ON waiting.schedule_id = schedule.id \n" +
            "INNER JOIN theme ON schedule.theme_id = theme.id \n" +
            "WHERE waiting.member_id = ?;";

    private static final String DELETE_BY_ID_SQL = "DELETE FROM waiting WHERE id = ?";

    public PreparedStatement createInsert(Connection connection, Waiting waiting) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
        setWaiting(ps, waiting);

        return ps;
    }

    private void setWaiting(PreparedStatement ps, Waiting waiting) throws SQLException {
        ps.setLong(1, waiting.getMemberId());
        ps.setLong(2, waiting.getScheduleId());
    }

    public PreparedStatement createSelectById(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL);
        ps.setLong(1, id);

        return ps;
    }

    public PreparedStatement createSelectByScheduleId(Connection connection, Long scheduleId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_SCHEDULE_ID_SQL);
        ps.setLong(1, scheduleId);

        return ps;
    }

    public PreparedStatement createSelectByMemberId(Connection connection, Long memberId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_MEMBER_ID_SQL);
        ps.setLong(1, memberId);

        return ps;
    }

    public PreparedStatement createDeleteById(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DELETE_BY_ID_SQL);
        ps.setLong(1, id);

        return ps;
    }
}