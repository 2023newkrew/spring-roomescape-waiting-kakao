package nextstep.reservation.repository;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.repository.jdbc.ReservationResultSetParser;
import nextstep.reservation.repository.jdbc.ReservationStatementCreator;
import nextstep.schedule.domain.Schedule;
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
    public boolean existsBySchedule(Schedule schedule) {
        return Boolean.TRUE.equals(
                jdbcTemplate.query(
                        connection -> statementCreator.createSelectByScheduleId(connection, schedule.getId()),
                        ResultSet::next
                ));
    }

    @Override
    public boolean existsByMemberAndSchedule(Reservation reservation) {
        return Boolean.TRUE.equals(
                jdbcTemplate.query(
                        connection -> statementCreator.selectByMemberIdAndScheduleId(
                                connection,
                                reservation.getMemberId(),
                                reservation.getScheduleId()
                        ),
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
    public List<Reservation> getByMember(Member member) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByMemberId(connection, member.getId()),
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