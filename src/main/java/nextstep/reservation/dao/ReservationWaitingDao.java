package nextstep.reservation.dao;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.model.ReservationWaiting;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationWaitingDao {
    public final JdbcTemplate jdbcTemplate;

    public int countByScheduleId(Long scheduleId) {
        String sql = "SELECT count(*) FROM reservation_waiting WHERE schedule_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, scheduleId);
    }

    private final RowMapper<ReservationWaiting> rowMapper = (resultSet, rowNum) -> new ReservationWaiting(
            resultSet.getLong("id"),
            resultSet.getLong("member_id"),
            resultSet.getLong("schedule_id"),
            resultSet.getTimestamp("applied_time").toLocalDateTime()
    );

    public Optional<ReservationWaiting> findById(Long id) {
        String sql = "SELECT id, member_id, schedule_id, applied_time FROM reservation_waiting WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    public List<ReservationWaiting> findByMemberId(Long memberId) {
        String sql = "SELECT id, member_id, schedule_id, applied_time FROM reservation_waiting WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Long countBeforeAppliedTime(Long scheduleId, LocalDateTime appliedTime) {
        String sql = "SELECT count(*) FROM reservation_waiting WHERE schedule_id = ? AND applied_time < ?";
        return jdbcTemplate.queryForObject(sql, Long.class, scheduleId, appliedTime);
    }

    public Long save(ReservationWaiting reservationWaiting){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO reservation_waiting(member_id, schedule_id, applied_time) VALUES(?, ?, ?)";

        jdbcTemplate.update((connection) -> {
            PreparedStatement psmt = connection.prepareStatement(sql);
            int paramIndex = 1;
            psmt.setLong(paramIndex++, reservationWaiting.getMemberId());
            psmt.setLong(paramIndex++, reservationWaiting.getScheduleId());
            psmt.setTimestamp(paramIndex++, Timestamp.valueOf(reservationWaiting.getAppliedTime()));
            return psmt;
        }, keyHolder);

        return keyHolder.getKeyAs(Long.class);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_waiting WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
