package nextstep.waitingreservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class WaitingReservationDao {
    public final JdbcTemplate jdbcTemplate;
    private static final String DEFAULT_SELECT_FROM_SQL =
            "SELECT " +
                    "waiting_reservation.id, waiting_reservation.schedule_id, waiting_reservation.member_id, " +
                    "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                    "theme.id, theme.name, theme.desc, theme.price, " +
                    "member.id, member.username, member.password, member.name, member.phone, member.role, " +
                    "waiting_reservation.wait_num " +
                    "from waiting_reservation " +
                    "inner join schedule on waiting_reservation.schedule_id = schedule.id " +
                    "inner join theme on schedule.theme_id = theme.id " +
                    "inner join member on waiting_reservation.member_id = member.id ";

    public WaitingReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<WaitingReservation> rowMapper = (resultSet, rowNum) -> new WaitingReservation(
            resultSet.getLong("waiting_reservation.id"),
            new Schedule(
                    resultSet.getLong("schedule.id"),
                    new Theme(
                            resultSet.getLong("theme.id"),
                            resultSet.getString("theme.name"),
                            resultSet.getString("theme.desc"),
                            resultSet.getInt("theme.price")
                    ),
                    resultSet.getDate("schedule.date").toLocalDate(),
                    resultSet.getTime("schedule.time").toLocalTime()
            ),
            new Member(
                    resultSet.getLong("member.id"),
                    resultSet.getString("member.username"),
                    resultSet.getString("member.password"),
                    resultSet.getString("member.name"),
                    resultSet.getString("member.phone"),
                    resultSet.getString("member.role")
            ),
            resultSet.getLong("waiting_reservation.wait_num")
    );

    public Long save(WaitingReservation waitingReservation) {
        String sql = "INSERT INTO waiting_reservation (schedule_id, member_id, wait_num) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, waitingReservation.getSchedule().getId());
            ps.setLong(2, waitingReservation.getMember().getId());
            ps.setLong(3, waitingReservation.getWaitNum());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<WaitingReservation> findById(Long id) {
        String sql = DEFAULT_SELECT_FROM_SQL +
                "where waiting_reservation.id = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<WaitingReservation> findByScheduleIdAndWaitNum(Long scheduleId, Long waitNum) {
        String sql = DEFAULT_SELECT_FROM_SQL +
                "where schedule.id = ? and waiting_reservation.wait_num = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, scheduleId, waitNum));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<WaitingReservation> findAllByMemberId(Long memberId) {
        String sql = DEFAULT_SELECT_FROM_SQL +
                "where member.id = ?;";
        try {
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<WaitingReservation> findAllByScheduleId(Long scheduleId) {
        String sql = DEFAULT_SELECT_FROM_SQL +
                "where schedule.id = ?;";
        try {
            return jdbcTemplate.query(sql, rowMapper, scheduleId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Long countByScheduleId(Long scheduleId) {
        String sql = "SELECT count(id) from waiting_reservation where schedule_id = ?;";
        return jdbcTemplate.queryForObject(sql, Long.class, scheduleId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM waiting_reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public void adjustWaitNumByScheduleIdAndBaseNum(Long scheduleId, Long baseWaitNum) {
        String sql = "UPDATE waiting_reservation set wait_num = wait_num - 1 where schedule_id = ? and wait_num > ?";
        jdbcTemplate.update(sql, scheduleId, baseWaitNum);
    }

}
