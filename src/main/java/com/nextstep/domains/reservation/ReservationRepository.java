package com.nextstep.domains.reservation;

import com.nextstep.domains.reservation.dao.ReservationStatementCreator;
import com.nextstep.domains.reservation.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import com.nextstep.domains.reservation.dao.ReservationResultSetParser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ReservationStatementCreator statementCreator;

    private final ReservationResultSetParser resultSetParser;


    public boolean existsByScheduleId(Long scheduleId) {
        return Boolean.TRUE.equals(
                jdbcTemplate.query(
                        connection -> statementCreator.createSelectByScheduleId(connection, scheduleId),
                        ResultSet::next
                ));
    }


    public boolean existsByMemberIdAndScheduleId(Long memberId, Long scheduleId) {
        return Boolean.TRUE.equals(
                jdbcTemplate.query(
                        connection -> statementCreator.selectByMemberIdAndScheduleId(connection, memberId, scheduleId),
                        ResultSet::next
                ));
    }


    public Reservation insert(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, reservation),
                keyHolder
        );
        reservation.setId(keyHolder.getKeyAs(Long.class));

        return reservation;
    }


    public Reservation getById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseReservation
        );
    }


    public List<Reservation> getByMemberId(Long memberId) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByMemberId(connection, memberId),
                resultSetParser::parseReservations
        );
    }


    public boolean updateById(Long id, ReservationStatus status) {
        int updatedRow = jdbcTemplate.update(connection -> statementCreator.createUpdateById(connection, id, status));

        return updatedRow > 0;
    }


    public boolean deleteById(Long id) {
        int deletedRow = jdbcTemplate.update(connection -> statementCreator.createDeleteById(connection, id));

        return deletedRow > 0;
    }

}