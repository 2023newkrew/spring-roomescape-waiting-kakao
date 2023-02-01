package nextstep.utils;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationState;
import nextstep.reservation_waiting.ReservationWaiting;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.RowMapper;

public class RowMapperUtil {

    public static final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation.id"),
            new Schedule(
                    resultSet.getLong("schedule.id"),
                    new Theme(
                            resultSet.getLong("theme.id"),
                            resultSet.getString("theme.name"),
                            resultSet.getString("theme.desc"),
                            resultSet.getInt("theme.price")
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
            ),
            resultSet.getTimestamp("reservation.created_datetime").toLocalDateTime(),
            ReservationState.from(resultSet.getString("reservation.state"))
    );

    public static final RowMapper<ReservationWaiting> reservationWaitingRowMapper = (resultSet, rowNum) -> new ReservationWaiting(
            reservationRowMapper.mapRow(resultSet, rowNum),
            resultSet.getInt("wait_num")
    );

    public static final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("phone"),
            resultSet.getString("role")
    );

    public static final RowMapper<Schedule> scheduleRowMapper = (resultSet, rowNum) -> new Schedule(
            resultSet.getLong("schedule.id"),
            new Theme(
                    resultSet.getLong("theme.id"),
                    resultSet.getString("theme.name"),
                    resultSet.getString("theme.desc"),
                    resultSet.getInt("theme.price")
            ),
            resultSet.getDate("schedule.date").toLocalDate(),
            resultSet.getTime("schedule.time").toLocalTime()
    );

    public static final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("desc"),
            resultSet.getInt("price")
    );
}
