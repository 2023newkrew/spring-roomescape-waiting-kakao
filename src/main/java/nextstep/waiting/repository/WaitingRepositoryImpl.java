package nextstep.waiting.repository;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.repository.jdbc.ReservationStatementCreator;
import nextstep.waiting.domain.Waiting;
import nextstep.waiting.repository.jdbc.WaitingResultSetParser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class WaitingRepositoryImpl implements WaitingRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ReservationStatementCreator statementCreator;

    private final WaitingResultSetParser resultSetParser;

    @Override
    public Waiting insert(Waiting waiting) {
        return null;
    }

    @Override
    public Waiting getById(Long id) {
        return null;
    }

    @Override
    public List<Waiting> getByMemberId(Long memberId) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}