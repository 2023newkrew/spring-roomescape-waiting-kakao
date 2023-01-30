package nextstep.reservation.repository.jdbc;

import auth.domain.UserRole;
import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;
import nextstep.schedule.domain.Schedule;
import nextstep.theme.domain.Theme;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ReservationResultSetParser {

    public List<Reservation> parseReservations(ResultSet resultSet) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = parseReservation(resultSet);
        while (Objects.nonNull(reservation)) {
            reservations.add(reservation);
            reservation = parseReservation(resultSet);
        }

        return reservations;
    }

    public Reservation parseReservation(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        return new Reservation(
                resultSet.getLong("reservation.id"),
                parseMember(resultSet), parseSchedule(resultSet)
        );
    }

    private Member parseMember(ResultSet resultSet) throws SQLException {
        return new Member(
                resultSet.getLong("member.id"),
                resultSet.getString("member.username"),
                resultSet.getString("member.password"),
                resultSet.getString("member.name"),
                resultSet.getString("member.phone"),
                UserRole.valueOf(resultSet.getString("member.role"))
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

    public Long parseKey(ResultSet resultSet) throws SQLException {
        resultSet.next();

        return resultSet.getLong(1);
    }
}
