package nextstep.reservationwaiting.repository;

import nextstep.member.domain.Member;
import nextstep.reservationwaiting.domain.ReservationWaiting;
import nextstep.schedule.domain.Schedule;
import nextstep.theme.domain.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
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
            resultSet.getTimestamp("reservation_waiting.created_at").toLocalDateTime()
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO RESERVATION_WAITING (schedule_id, member_id, created_at) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setLong(2, reservationWaiting.getMember().getId());
            ps.setTimestamp(3, Timestamp.valueOf(reservationWaiting.getCreatedAt()));
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<ReservationWaiting> findAllByMemberId(Long memberId) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.created_at, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "FROM reservation_waiting " +
                "INNER JOIN schedule ON reservation_waiting.schedule_id = schedule.id " +
                "INNER JOIN theme ON schedule.theme_id = theme.id " +
                "INNER JOIN member ON reservation_waiting.member_id = member.id " +
                "WHERE reservation_waiting.member_id = ?;";

        try{
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch (Exception e) {
            return new ArrayList<ReservationWaiting>();
        }
    }

    public int getWaitNum(Long scheduleId, LocalDateTime createdAt) {
        String sql = "SELECT COUNT(*) FROM (SELECT id FROM reservation_waiting WHERE schedule_id = ? AND created_at <= ? LIMIT 100)";

        return jdbcTemplate.queryForObject(sql, Integer.class, scheduleId, createdAt);
    }

    public ReservationWaiting findById(Long id) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.created_at, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation_waiting " +
                "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation_waiting.member_id = member.id " +
                "where reservation_waiting.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public ReservationWaiting findByScheduleId(Long id) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.created_at, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation_waiting " +
                "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation_waiting.member_id = member.id " +
                "where reservation_waiting.schedule_id = ?" +
                "ORDER BY reservation_waiting.id ASC LIMIT 1;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_waiting where id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
