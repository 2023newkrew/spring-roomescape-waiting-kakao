package nextstep.utils;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationState;
import nextstep.reservation_waiting.ReservationWaiting;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.RowMapper;

public class RowMapperUtil {

    public static final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("phone"),
            resultSet.getString("role")
    );

    public static final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("desc"),
            resultSet.getInt("price")
    );

    public static final RowMapper<Schedule> scheduleRowMapper = (resultSet, rowNum) -> new Schedule(
            resultSet.getLong("schedule.id"),
            themeRowMapper.mapRow(resultSet, rowNum),
            resultSet.getDate("schedule.date").toLocalDate(),
            resultSet.getTime("schedule.time").toLocalTime()
    );

    public static final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation.id"),
            scheduleRowMapper.mapRow(resultSet, rowNum),
            memberRowMapper.mapRow(resultSet, rowNum),
            resultSet.getTimestamp("reservation.created_datetime").toLocalDateTime(),
            ReservationState.from(resultSet.getString("reservation.state"))
    );

    public static final RowMapper<ReservationWaiting> reservationWaitingRowMapper = (resultSet, rowNum) -> new ReservationWaiting(
            reservationRowMapper.mapRow(resultSet, rowNum),
            resultSet.getInt("wait_num")
    );

}
