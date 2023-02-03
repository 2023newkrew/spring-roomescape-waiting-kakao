package roomwaiting.nextstep.dbmapper;
import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.ReservationStatus;
import roomwaiting.nextstep.reservation.domain.Reservation;
import roomwaiting.nextstep.reservation.domain.ReservationWaiting;
import roomwaiting.nextstep.sales.Sales;
import roomwaiting.nextstep.schedule.Schedule;
import roomwaiting.nextstep.theme.Theme;
import org.springframework.jdbc.core.RowMapper;

public class H2Mapper implements DatabaseMapper {
    public RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNum) ->
            new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("role")
            );
    }

    public RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("reservation.id"),
                new Schedule(
                        resultSet.getLong("schedule.id"),
                        new Theme(
                                resultSet.getLong("theme.id"),
                                resultSet.getString("theme.name"),
                                resultSet.getString("theme.description"),
                                resultSet.getLong("theme.price")
                        ),
                        resultSet.getDate("schedule.date").toLocalDate(),
                        resultSet.getTime("schedule.time").toLocalTime()
                ),
                new Member(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("role")
                ),
                ReservationStatus.valueOf(resultSet.getString("status"))
        );
    }

    public RowMapper<Schedule> scheduleRowMapper() {
        return (resultSet, rowNum) -> new Schedule(
                resultSet.getLong("schedule.id"),
                new Theme(
                        resultSet.getLong("theme.id"),
                        resultSet.getString("theme.name"),
                        resultSet.getString("theme.description"),
                        resultSet.getLong("theme.price")
                ),
                resultSet.getDate("schedule.date").toLocalDate(),
                resultSet.getTime("schedule.time").toLocalTime()
        );
    }

    public RowMapper<Theme> themeRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getLong("price")
        );
    }

    public RowMapper<ReservationWaiting> reservationWaitingRowMapper() {
        return (resultSet, rowNum) -> new ReservationWaiting(
                resultSet.getLong("reservation_waiting.id"),
                new Schedule(
                        resultSet.getLong("schedule.id"),
                        new Theme(
                                resultSet.getLong("theme.id"),
                                resultSet.getString("theme.name"),
                                resultSet.getString("theme.description"),
                                resultSet.getLong("theme.price")
                        ),
                        resultSet.getDate("schedule.date").toLocalDate(),
                        resultSet.getTime("schedule.time").toLocalTime()
                ),
                new Member(
                        resultSet.getLong("member.id"),
                        resultSet.getString("member.username"),
                        resultSet.getString("member.password"),
                        resultSet.getString("member.name"),
                        resultSet.getString("member.phone"),
                        resultSet.getString("member.role")
                ), resultSet.getLong("reservation_waiting.wait_num")
        );
    }

    @Override
    public RowMapper<Sales> salesRowMapper() {
        return (resultSet, rowNum) -> new Sales(
                resultSet.getLong("sales.id"),
                new Member(
                        resultSet.getLong("member.id"),
                        resultSet.getString("member.username"),
                        resultSet.getString("member.password"),
                        resultSet.getString("member.name"),
                        resultSet.getString("member.phone"),
                        resultSet.getString("member.role")
                ),
                resultSet.getLong("sales.price"),
                new Schedule(
                    resultSet.getLong("schedule.id"),
                    new Theme(
                            resultSet.getLong("theme.id"),
                            resultSet.getString("theme.name"),
                            resultSet.getString("theme.description"),
                            resultSet.getLong("theme.price")
                    ),
                    resultSet.getDate("schedule.date").toLocalDate(),
                    resultSet.getTime("schedule.time").toLocalTime()
                )
        );
    }
}
