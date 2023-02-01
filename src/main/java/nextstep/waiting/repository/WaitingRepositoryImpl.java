package nextstep.waiting.repository;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.MemberEntity;
import nextstep.schedule.domain.ScheduleEntity;
import nextstep.waiting.domain.WaitingEntity;
import nextstep.waiting.repository.jdbc.WaitingResultSetParser;
import nextstep.waiting.repository.jdbc.WaitingStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class WaitingRepositoryImpl implements WaitingRepository {

    private final JdbcTemplate jdbcTemplate;

    private final WaitingStatementCreator statementCreator;

    private final WaitingResultSetParser resultSetParser;

    @Override
    public WaitingEntity insert(WaitingEntity waiting) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, waiting),
                keyHolder
        );
        waiting.setId(keyHolder.getKeyAs(Long.class));

        return waiting;
    }

    @Override
    public WaitingEntity getById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseWaiting
        );
    }

    @Override
    public WaitingEntity getFirstBySchedule(ScheduleEntity schedule) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByScheduleId(connection, schedule.getId()),
                resultSetParser::parseWaiting
        );
    }

    @Override
    public List<WaitingEntity> getByMember(MemberEntity member) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByMemberId(connection, member.getId()),
                resultSetParser::parseWaitings
        );
    }

    @Override
    public boolean deleteById(Long id) {
        int deletedRow = jdbcTemplate.update(connection -> statementCreator.createDeleteById(connection, id));

        return deletedRow > 0;
    }
}