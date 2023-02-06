package nextstep.waiting.repository;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.schedule.domain.Schedule;
import nextstep.waiting.domain.Waiting;
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
    public Waiting insert(Waiting waiting) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, waiting),
                keyHolder
        );
        waiting.setId(keyHolder.getKeyAs(Long.class));

        return waiting;
    }

    @Override
    public Waiting getById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseWaiting
        );
    }

    @Override
    public Waiting getFirstBySchedule(Schedule schedule) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByScheduleId(connection, schedule.getId()),
                resultSetParser::parseWaiting
        );
    }

    @Override
    public List<Waiting> getAllByMember(Member member) {
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