package nextstep.web.reservation_waiting;

import nextstep.web.member.Member;
import nextstep.web.schedule.Schedule;
import nextstep.web.theme.Theme;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReservationWaitingDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationWaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationWaiting> rowMapper = (resultSet, rowNum) -> new ReservationWaiting(
            resultSet.getLong("reservation_waiting.id"),
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
            resultSet.getInt("reservation_waiting.wait_num")
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation_waiting (schedule_id, member_id, wait_num) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setLong(2, reservationWaiting.getMember().getId());
            ps.setInt(3, reservationWaiting.getWaitNum());
            return ps;

        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<ReservationWaiting> findAllByScheduleIdOrderByDesc(Long scheduleId) {
        String sql = "SELECT reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, reservation_waiting.wait_num, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "FROM reservation_waiting " +
                "JOIN schedule ON reservation_waiting.schedule_id=schedule.id " +
                "JOIN theme ON theme.id=schedule.theme_id " +
                "JOIN member ON reservation_waiting.member_id=member.id " +
                "WHERE reservation_waiting.schedule_id = ? " +
                "ORDER BY reservation_waiting.wait_num DESC;";

        return jdbcTemplate.query(sql, rowMapper, scheduleId);
    }

    public List<ReservationWaiting> findAllByMember(Member member) {
        String sql = "SELECT reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, reservation_waiting.wait_num, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "FROM reservation_waiting " +
                "JOIN schedule ON reservation_waiting.schedule_id=schedule.id " +
                "JOIN theme ON theme.id=schedule.theme_id " +
                "JOIN member ON reservation_waiting.member_id=member.id " +
                "WHERE reservation_waiting.member_id = ?;";

        return jdbcTemplate.query(sql, rowMapper, member.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_waiting WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public Optional<ReservationWaiting> findById(Long id) {
        String sql = "SELECT reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, reservation_waiting.wait_num, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "FROM reservation_waiting " +
                "JOIN schedule ON reservation_waiting.schedule_id=schedule.id " +
                "JOIN theme ON theme.id=schedule.theme_id " +
                "JOIN member ON reservation_waiting.member_id=member.id " +
                "WHERE reservation_waiting.id = ?;";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
