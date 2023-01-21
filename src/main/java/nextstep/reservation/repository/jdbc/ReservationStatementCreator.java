package nextstep.reservation.repository.jdbc;

import nextstep.reservation.domain.Reservation;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class ReservationStatementCreator {

    private static final String
            SELECT_BY_DATE_AND_TIME_SQL =
            "SELECT * FROM reservation WHERE date = ? AND time = ? AND theme_id = ? LIMIT 1";

    private static final String INSERT_SQL = "INSERT INTO reservation(date, time, name, theme_id) VALUES(?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "        SELECT " +
                    "   r.id,                   r.date, " +
                    "   r.time,                 r.name, " +
                    "   t.id   AS theme_id,     t.name AS theme_name, " +
                    "   t.desc AS theme_desc,   t.price AS theme_price " +
                    "FROM reservation AS r " +
                    "INNER JOIN theme AS t " +
                    "   ON r.theme_id = t.id " +
                    "WHERE r.id = ?";

    private static final String DELETE_BY_ID_SQL = "DELETE FROM reservation WHERE id = ?";

    public PreparedStatement createSelectByTimetable(
            Connection connection, Reservation reservation) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_DATE_AND_TIME_SQL);
        LocalDate date = reservation.getDate();
        LocalTime time = reservation.getTime();
        ps.setDate(1, Date.valueOf(date));
        ps.setTime(2, Time.valueOf(time));
        ps.setLong(3, reservation.getThemeId());

        return ps;
    }

    public PreparedStatement createInsert(
            Connection connection, Reservation reservation) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
        setReservation(ps, reservation);

        return ps;
    }

    private void setReservation(PreparedStatement ps, Reservation reservation) throws SQLException {
        LocalDate date = reservation.getDate();
        LocalTime time = reservation.getTime();
        ps.setDate(1, Date.valueOf(date));
        ps.setTime(2, Time.valueOf(time));
        ps.setString(3, reservation.getName());
        ps.setLong(4, reservation.getTheme()
                .getId()
        );
    }

    public PreparedStatement createSelectById(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL);
        ps.setLong(1, id);

        return ps;
    }

    public PreparedStatement createDeleteById(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DELETE_BY_ID_SQL);
        ps.setLong(1, id);

        return ps;
    }

}