package nextstep.waiting.repository.jdbc;

import nextstep.waiting.domain.Waiting;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class WaitingResultSetParser {


    public Waiting parseWaiting(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        return null;
    }
}
