package nextstep.member.repository.jdbc;

import auth.domain.UserRole;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MemberResultSetParser {

    public Member parseMember(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        return new Member(
                resultSet.getLong("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                resultSet.getString("phone"),
                UserRole.valueOf(resultSet.getString("role"))
        );
    }
}
