package nextstep.member.repository;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.MemberEntity;
import nextstep.member.repository.jdbc.MemberResultSetParser;
import nextstep.member.repository.jdbc.MemberStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    private final MemberStatementCreator statementCreator;

    private final MemberResultSetParser resultSetParser;

    @Override
    public MemberEntity insert(MemberEntity reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, reservation),
                keyHolder
        );
        reservation.setId(keyHolder.getKeyAs(Long.class));

        return reservation;
    }

    @Override
    public MemberEntity getById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseMember
        );
    }

    @Override
    public MemberEntity getByUsername(String username) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByUsername(connection, username),
                resultSetParser::parseMember
        );
    }

}