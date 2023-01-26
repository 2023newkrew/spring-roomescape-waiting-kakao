package nextstep.waiting.repository;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.repository.jdbc.ReservationStatementCreator;
import nextstep.waiting.repository.jdbc.WaitingResultSetParser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class WaitingRepositoryImpl implements WaitingRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ReservationStatementCreator statementCreator;

    private final WaitingResultSetParser resultSetParser;
}