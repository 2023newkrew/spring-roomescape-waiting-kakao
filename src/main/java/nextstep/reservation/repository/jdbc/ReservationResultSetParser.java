package nextstep.reservation.repository.jdbc;

import nextstep.reservation.domain.Reservation;
import nextstep.theme.domain.Theme;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

@Component
public class ReservationResultSetParser {

    public Reservation parseReservation(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Date date = resultSet.getDate("date");
        Time time = resultSet.getTime("time");

        return new Reservation(
                resultSet.getLong("id"),
                date.toLocalDate(),
                time.toLocalTime(),
                resultSet.getString("name"),
                parseTheme(resultSet)
        );
    }

    private Theme parseTheme(ResultSet resultSet) throws SQLException {
        return new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_desc"),
                resultSet.getInt("theme_price")
        );
    }

    public Long parseKey(ResultSet resultSet) throws SQLException {
        resultSet.next();

        return resultSet.getLong(1);
    }
}
