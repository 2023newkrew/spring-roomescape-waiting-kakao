package com.nextstep.domains.sales.dao;

import com.nextstep.domains.sales.Sales;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class SalesStatementCreator {
    private static final String INSERT_SQL = "INSERT INTO sales (reservation_id, amount, status) VALUES (?, ?, ?);";

    private static final String
            SELECT_BY_RESERVATION_ID_SQL = "SELECT * FROM sales WHERE reservation_id = ?";

    private static final String
            SELECT_ALL_SQL = "SELECT sales.id, sales.amount, sales.status, " +
            "reservation.id, reservation.status,  " +
            "member.id, member.username, member.password, member.name, member.phone, member.role, " +
            "schedule.id, schedule.date, schedule.time, " +
            "   theme.id, theme.name, theme.desc, theme.price " +
            "FROM sales " +
            "inner join reservation on sales.reservation_id = reservation.id " +
            "inner join member on reservation.member_id = member.id " +
            "inner join schedule on reservation.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id ";


    private static final String UPDATE_AMOUNT_AND_STATUS_BY_ID_SQL = "UPDATE sales SET amount = ?, status = ? WHERE id = ?";


    public PreparedStatement createInsert(
            Connection connection, Sales sales) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
        ps.setLong(1, sales.getReservationId());
        ps.setLong(2, sales.getAmount());
        ps.setString(3, sales.getStatus().name());

        return ps;
    }

    public PreparedStatement createSelectByReservationId(Connection connection, Long reservationId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_BY_RESERVATION_ID_SQL);
        ps.setLong(1, reservationId);

        return ps;
    }

    public PreparedStatement createSelectAll(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
        return ps;
    }

    public PreparedStatement createUpdateById(Connection connection, Sales sales) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(UPDATE_AMOUNT_AND_STATUS_BY_ID_SQL);
        ps.setLong(3, sales.getId());
        ps.setString(2, sales.getStatus().name());
        ps.setInt(1, sales.getAmount());

        return ps;
    }

}
