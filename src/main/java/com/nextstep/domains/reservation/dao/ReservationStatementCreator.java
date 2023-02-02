package com.nextstep.domains.reservation.dao;

import com.nextstep.domains.reservation.Reservation;
import com.nextstep.domains.reservation.enums.StatusType;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class ReservationStatementCreator {

    private static final String SELECT_BY_SCHEDULE_ID_SQL = "SELECT * FROM reservation WHERE schedule_id = ? AND deleted = false";

    private static final String
            SELECT_BY_MEMBER_ID_AND_SCHEDULE_ID_SQL =
            "SELECT * FROM reservation WHERE member_id = ? AND schedule_id = ?";

    private static final String INSERT_SQL = "INSERT INTO reservation (member_id, schedule_id) VALUES (?, ?);";

    private static final String
            SELECT_BY_ID_SQL =
            "SELECT reservation.id, reservation.status, " +
                    "member.id, member.username, member.password, member.name, member.phone, member.role, " +
                    "schedule.id, schedule.date, schedule.time, " +
                    "   theme.id, theme.name, theme.desc, theme.price " +
                    "from reservation " +
                    "inner join member on reservation.member_id = member.id " +
                    "inner join schedule on reservation.schedule_id = schedule.id " +
                    "inner join theme on schedule.theme_id = theme.id " +
                    "where reservation.id = ? AND reservation.deleted = false;";

    private static final String
            SELECT_BY_MEMBER_ID_SQL =
            "SELECT reservation.id, reservation.status,  " +
                    "member.id, member.username, member.password, member.name, member.phone, member.role, " +
                    "schedule.id, schedule.date, schedule.time, " +
                    "   theme.id, theme.name, theme.desc, theme.price " +
                    "from reservation " +
                    "inner join member on reservation.member_id = member.id " +
                    "inner join schedule on reservation.schedule_id = schedule.id " +
                    "inner join theme on schedule.theme_id = theme.id " +
                    "where reservation.member_id = ?;";

    private static final String UPDATE_STATUS_BY_ID_SQL = "UPDATE reservation SET status = ? WHERE id = ?";
    
    private static final String DELETE_BY_ID_SQL = "UPDATE reservation SET deleted = true WHERE id = ?";

    public PreparedStatement createSelectByScheduleId(
            Connection connection, Long scheduleId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_SCHEDULE_ID_SQL);
        ps.setLong(1, scheduleId);

        return ps;
    }

    public PreparedStatement selectByMemberIdAndScheduleId(
            Connection connection, Long memberId, Long scheduleId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_MEMBER_ID_AND_SCHEDULE_ID_SQL);
        ps.setLong(1, memberId);
        ps.setLong(2, scheduleId);

        return ps;
    }

    public PreparedStatement createInsert(
            Connection connection, Reservation reservation) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
        ps.setLong(1, reservation.getMemberId());
        ps.setLong(2, reservation.getScheduleId());

        return ps;
    }

    public PreparedStatement createSelectById(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL);
        ps.setLong(1, id);

        return ps;
    }

    public PreparedStatement createSelectByMemberId(Connection connection, Long memberId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_MEMBER_ID_SQL);
        ps.setLong(1, memberId);

        return ps;
    }

    public PreparedStatement createUpdateById(Connection connection, Long id, StatusType status) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(UPDATE_STATUS_BY_ID_SQL);
        ps.setLong(2, id);
        ps.setString(1, status.name());

        return ps;
    }

    public PreparedStatement createDeleteById(Connection connection, Long id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DELETE_BY_ID_SQL);
        ps.setLong(1, id);

        return ps;
    }

}