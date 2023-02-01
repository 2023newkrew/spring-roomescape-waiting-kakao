package com.nextstep.domains.waiting;

import lombok.RequiredArgsConstructor;
import com.nextstep.domains.waiting.dao.WaitingResultSetParser;
import com.nextstep.domains.waiting.dao.WaitingStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class WaitingRepository {

    private final JdbcTemplate jdbcTemplate;

    private final WaitingStatementCreator statementCreator;

    private final WaitingResultSetParser resultSetParser;


    public Waiting insert(Waiting waiting) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, waiting),
                keyHolder
        );
        waiting.setId(keyHolder.getKeyAs(Long.class));

        return waiting;
    }


    public Waiting getById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseWaiting
        );
    }


    public Waiting getFirstByReservationId(Long reservationId) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByScheduleId(connection, reservationId),
                resultSetParser::parseWaiting
        );
    }


    public List<Waiting> getByMemberId(Long memberId) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByMemberId(connection, memberId),
                resultSetParser::parseWaitings
        );
    }


    public boolean deleteById(Long id) {
        int deletedRow = jdbcTemplate.update(connection -> statementCreator.createDeleteById(connection, id));

        return deletedRow > 0;
    }
}