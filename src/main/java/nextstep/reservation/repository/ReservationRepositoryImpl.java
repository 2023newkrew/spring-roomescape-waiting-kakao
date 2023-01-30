package nextstep.reservation.repository;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.repository.jdbc.ReservationResultSetParser;
import nextstep.reservation.repository.jdbc.ReservationStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ReservationStatementCreator statementCreator;

    private final ReservationResultSetParser resultSetParser;

    @Override
    public boolean existsByScheduleId(Long scheduleId) {
        return Boolean.TRUE.equals(
                jdbcTemplate.query(
                        connection -> statementCreator.createSelectByScheduleId(connection, scheduleId),
                        ResultSet::next
                ));
    }

    @Override
    public boolean existsByMemberIdAndScheduleId(Long memberId, Long scheduleId) {
        return Boolean.TRUE.equals(
                jdbcTemplate.query(
                        connection -> statementCreator.selectByMemberIdAndScheduleId(connection, memberId, scheduleId),
                        ResultSet::next
                ));
    }

    @Override
    public Reservation insert(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, reservation),
                keyHolder
        );
        reservation.setId(keyHolder.getKeyAs(Long.class));

        return reservation;
    }

    @Override
    public Reservation getById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseReservation
        );
    }

    @Override
    public List<Reservation> getByMemberId(Long memberId) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByMemberId(connection, memberId),
                resultSetParser::parseReservations
        );
    }

    @Override
    public boolean updateById(Long id, Long memberId) {
        int updatedRow = jdbcTemplate.update(connection -> statementCreator.createUpdateById(connection, id, memberId));

        return updatedRow > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        int deletedRow = jdbcTemplate.update(connection -> statementCreator.createDeleteById(connection, id));

        return deletedRow > 0;
    }

}