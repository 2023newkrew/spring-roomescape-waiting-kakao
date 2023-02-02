package com.nextstep.domains.sales.dao;

import com.authorizationserver.domains.authorization.enums.RoleType;
import com.nextstep.domains.member.Member;
import com.nextstep.domains.reservation.Reservation;
import com.nextstep.domains.reservation.enums.ReservationStatus;
import com.nextstep.domains.sales.Sales;
import com.nextstep.domains.sales.enums.SalesStatus;
import com.nextstep.domains.schedule.Schedule;
import com.nextstep.domains.theme.Theme;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class SalesResultSetParser {

    public List<Sales> parseSalesList(ResultSet resultSet) throws SQLException {
        List<Sales> salesList = new ArrayList<>();
        Sales sales = parseSales(resultSet);
        while (Objects.nonNull(sales)) {
            salesList.add(sales);
            sales = parseSales(resultSet);
        }

        return salesList;
    }

    public Sales parseSales(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        return new Sales(
                resultSet.getLong("sales.id"),
                parseReservation(resultSet),
                resultSet.getInt("sales.amount"),
                SalesStatus.valueOf(resultSet.getString("sales.status"))
        );
    }

    public Reservation parseReservation(ResultSet resultSet) throws SQLException {
        return new Reservation(
                resultSet.getLong("reservation.id"),
                parseMember(resultSet),
                parseSchedule(resultSet),
                ReservationStatus.valueOf(resultSet.getString("reservation.status"))
        );
    }

    private Member parseMember(ResultSet resultSet) throws SQLException {
        return new Member(
                resultSet.getLong("member.id"),
                resultSet.getString("member.username"),
                resultSet.getString("member.password"),
                resultSet.getString("member.name"),
                resultSet.getString("member.phone"),
                RoleType.valueOf(resultSet.getString("member.role"))
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
