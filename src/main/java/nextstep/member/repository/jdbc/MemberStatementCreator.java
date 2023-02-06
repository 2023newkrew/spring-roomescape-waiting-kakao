package nextstep.member.repository.jdbc;

import nextstep.member.domain.Member;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class MemberStatementCreator {

    private static final String
            INSERT_SQL =
            "INSERT INTO member (username, password, name, phone, role) VALUES (?, ?, ?, ?, ?);";

    private static final String
            SELECT_BY_ID_SQL =
            "SELECT id, username, password, name, phone, role from member where id = ?;";

    private static final String
            SELECT_BY_USERNAME_SQL =
            "SELECT id, username, password, name, phone, role from member where username = ?;";

    public PreparedStatement createInsert(
            Connection connection, Member reservation) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
        setMember(ps, reservation);

        return ps;
    }

    private void setMember(PreparedStatement ps, Member member) throws SQLException {
        ps.setString(1, member.getUsername());
        ps.setString(2, member.getPassword());
        ps.setString(3, member.getName());
        ps.setString(4, member.getPhone());
        ps.setString(5, member.getRole().toString());
    }

    public PreparedStatement createSelectById(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL);
        ps.setLong(1, id);

        return ps;
    }

    public PreparedStatement createSelectByUsername(Connection connection, String username) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_USERNAME_SQL);
        ps.setString(1, username);

        return ps;
    }

}