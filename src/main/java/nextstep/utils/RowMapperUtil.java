package nextstep.utils;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationStatus;
import nextstep.reservation.ReservationStatusHistory;
import nextstep.reservation_waiting.ReservationWaiting;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.RowMapper;

public class RowMapperUtil {

    public static final RowMapper<Member> memberRowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("member.id"),
            resultSet.getString("member.username"),
            resultSet.getString("member.password"),
            resultSet.getString("member.name"),
            resultSet.getString("member.phone"),
            resultSet.getString("member.role")
    );

    public static final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("theme.id"),
            resultSet.getString("theme.name"),
            resultSet.getString("theme.desc"),
            resultSet.getInt("theme.price")
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
            ReservationStatus.from(resultSet.getString("reservation.status"))
    );

    public static final RowMapper<ReservationWaiting> reservationWaitingRowMapper = (resultSet, rowNum) -> new ReservationWaiting(
            reservationRowMapper.mapRow(resultSet, rowNum),
            resultSet.getInt("wait_num")
    );

    public static final RowMapper<ReservationStatusHistory> reservationStatusHistoryMapper = (resultSet, rowNum) -> new ReservationStatusHistory(
            resultSet.getLong("reservation_status_history.id"),
            resultSet.getLong("reservation_status_history.reservation_id"),
            ReservationStatus.from(resultSet.getString("reservation_status_history.before_status")),
            ReservationStatus.from(resultSet.getString("reservation_status_history.after_status")),
            resultSet.getTimestamp("reservation_status_history.changed_datetime").toLocalDateTime()
    );

}
