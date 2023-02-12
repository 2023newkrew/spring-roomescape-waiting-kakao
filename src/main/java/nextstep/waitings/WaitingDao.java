package nextstep.waitings;

import nextstep.schedule.Schedule;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;


@Repository
public class WaitingDao {
    public final JdbcTemplate jdbcTemplate;

    public WaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Waiting> rowMapper = (resultSet, rowNum) -> Waiting.builder()
            .id(resultSet.getLong("waiting.id"))
            .schedule(Schedule.builder()
                    .id(resultSet.getLong("waiting.schedule_id"))
                    .build())
            .memberId(resultSet.getLong("waiting.member_id"))
            .build();

    public long save(final Long scheduleId, final Long memberId) {
        String sql = "INSERT INTO waiting (schedule_id, member_id) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, scheduleId);
            ps.setLong(2, memberId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Long countByScheduleId(final Long scheduleId) {
        String sql = "SELECT COUNT(*) FROM waiting WHERE schedule_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, scheduleId);
    }

    public Optional<Waiting> findById(final Long id) {
        String sql = "SELECT * FROM waiting WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch(DataAccessException e){
            return Optional.empty();
        }
    }

    public void deleteById(final Long waitingId){
        String sql = "DELETE FROM waiting where id = ?;";
        jdbcTemplate.update(sql, waitingId);
    }

    public List<Waiting> findAllByMemberId(Long memberId) {
        try {
            String sql = "SELECT * FROM waiting WHERE member_id = ?";
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch(DataAccessException e){
            return null;
        }
    }
}
