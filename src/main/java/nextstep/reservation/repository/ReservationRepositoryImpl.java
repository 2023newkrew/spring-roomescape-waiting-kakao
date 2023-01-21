package nextstep.reservation.repository;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.repository.jdbc.ReservationResultSetParser;
import nextstep.reservation.repository.jdbc.ReservationStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;

@RequiredArgsConstructor
@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ReservationStatementCreator statementCreator;

    private final ReservationResultSetParser resultSetParser;

    @Override
    public boolean existsByTimetable(Reservation reservation) {
        return Boolean.TRUE.equals(
                jdbcTemplate.query(
                        getExistsByTimetableStatementCreator(reservation),
                        ResultSet::next
                ));
    }

    private PreparedStatementCreator getExistsByTimetableStatementCreator(Reservation reservation) {
        return connection -> statementCreator.createSelectByTimetable(connection, reservation);
    }

    @Override
    public Reservation insert(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(getInsertStatementCreator(reservation), keyHolder);
        reservation.setId(keyHolder.getKeyAs(Long.class));

        return reservation;
    }

    private PreparedStatementCreator getInsertStatementCreator(Reservation reservation) {
        return connection -> statementCreator.createInsert(connection, reservation);
    }

    @Override
    public Reservation getById(Long id) {
        return jdbcTemplate.query(
                getSelectByIdStatementCreator(id),
                resultSetParser::parseReservation
        );
    }

    private PreparedStatementCreator getSelectByIdStatementCreator(Long id) {
        return connection -> statementCreator.createSelectById(connection, id);
    }

    @Override
    public boolean deleteById(Long id) {
        int deletedRow = jdbcTemplate.update(getDeleteByIdStatementCreator(id));

        return deletedRow > 0;
    }

    private PreparedStatementCreator getDeleteByIdStatementCreator(Long id) {
        return connection -> statementCreator.createDeleteById(connection, id);
    }
}