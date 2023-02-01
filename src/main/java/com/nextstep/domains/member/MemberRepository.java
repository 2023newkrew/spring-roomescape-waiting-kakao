package com.nextstep.domains.member;

import lombok.RequiredArgsConstructor;
import com.nextstep.domains.member.dao.MemberResultSetParser;
import com.nextstep.domains.member.dao.MemberStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepository{

    private final JdbcTemplate jdbcTemplate;

    private final MemberStatementCreator statementCreator;

    private final MemberResultSetParser resultSetParser;

    public Member insert(Member reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, reservation),
                keyHolder
        );
        reservation.setId(keyHolder.getKeyAs(Long.class));

        return reservation;
    }

    public Member getById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseMember
        );
    }

    public Member getByUsername(String username) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByUsername(connection, username),
                resultSetParser::parseMember
        );
    }

}