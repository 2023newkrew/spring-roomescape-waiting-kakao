package nextstep.reservationwaitings;

import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ReservationWaitingsDao {

    private final RowMapper<ReservationWaitings> rowMapper = (resultSet, rowNum) -> new ReservationWaitings(
            resultSet.getLong("reservation_waitings.id"),
            new Member(
                    resultSet.getLong("member.id"),
                    resultSet.getString("member.username"),
                    resultSet.getString("member.password"),
                    resultSet.getString("member.name"),
                    resultSet.getString("member.phone"),
                    resultSet.getString("member.role")
            ),
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

            resultSet.getLong("reservation_waitings.wait_num")
    );

    private final JdbcTemplate jdbcTemplate;

    public ReservationWaitingsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long findWaitNumByScheduleId(Long scheduleId) {
        String sql = "SELECT max(wait_num) as max_wait_num FROM reservation_waitings WHERE schedule_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("max_wait_num"), scheduleId);
    }

    public long create(ReservationWaitings reservationWaitings) {
        String sql = "INSERT INTO reservation_waitings (schedule_id, member_id, wait_num) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaitings.getSchedule().getId());
            ps.setLong(2, reservationWaitings.getMember().getId());
            ps.setLong(3, reservationWaitings.getWaitNum());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<ReservationWaitings> findMyReservationWaitings(Long id) {
        String sql = "SELECT " +
                "reservation_waitings.id, reservation_waitings.wait_num, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "FROM reservation_waitings " +
                "inner join schedule on reservation_waitings.schedule_id = schedule.id " +
                "inner join member on reservation_waitings.member_id = member.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "WHERE reservation_waitings.member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM reservation_waitings WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public ReservationWaitings findById(Long id) {
        String sql = "SELECT " +
                "reservation_waitings.id, reservation_waitings.wait_num, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "FROM reservation_waitings " +
                "inner join schedule on reservation_waitings.schedule_id = schedule.id " +
                "inner join member on reservation_waitings.member_id = member.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "WHERE reservation_waitings.id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
}
