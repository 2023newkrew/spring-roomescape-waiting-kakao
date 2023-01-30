package nextstep.reservationwaiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Collections;
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
            ReservationWaitingStatus.valueOf(resultSet.getString("reservation_waiting.status"))
    );

    private final RowMapper<ReservationWaiting> rowMapperWithWaitingNum = (resultSet, rowNum) -> new ReservationWaiting(
            resultSet.getLong("rw.id"),
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
            ReservationWaitingStatus.valueOf(resultSet.getString("rw.status"))
            , resultSet.getLong("waiting_num")
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation_waiting (schedule_id, member_id, status) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setLong(2, reservationWaiting.getMember().getId());
            ps.setString(3, reservationWaiting.getStatus().name());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public ReservationWaiting findById(Long id) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role, " +
                "reservation_waiting.status " +
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

    public Long getRank(ReservationWaiting reservationWaiting) {
        String sql = "SELECT COUNT(*) FROM reservation_waiting WHERE schedule_id = ? AND id < ?;";
        return jdbcTemplate.queryForObject(sql, Long.class, reservationWaiting.getSchedule().getId(), reservationWaiting.getId());
    }

    public void updateStatusById(Long id, ReservationWaitingStatus status) {
        String sql = "UPDATE reservation_waiting SET status = ? where id = ?;";
        jdbcTemplate.update(sql, status.name(), id);
    }

    public Collection<ReservationWaiting> findAllByMemberIdWithOrder(Long memberId) {
        String sql = "SELECT rw.id, rw.schedule_id, rw.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role, " +
                "rw.status, " +
                "waiting_num " +
                "from (" +
                "   select *, row_number() over(partition by reservation_waiting.schedule_id order by reservation_waiting.id asc) waiting_num " +
                "   from reservation_waiting" +
                "   where reservation_waiting.status = ? " +
                ") rw " +
                "inner join schedule on rw.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on rw.member_id = member.id " +
                "where rw.member_id = ?";
        try {
            return jdbcTemplate.query(sql, rowMapperWithWaitingNum, ReservationWaitingStatus.WAITING.name(), memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
