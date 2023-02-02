package nextstep.reservationwaiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Optional;

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
            ReservationWaitingStatus.valueOf(resultSet.getString("reservation_waiting.status")),
            resultSet.getTimestamp("reservation_waiting.created_at").toLocalDateTime()
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
            ReservationWaitingStatus.valueOf(resultSet.getString("rw.status")),
            resultSet.getTimestamp("rw.created_at").toLocalDateTime(),
            resultSet.getLong("waiting_num")
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation_waiting (schedule_id, member_id, status, created_at) VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setLong(2, reservationWaiting.getMember().getId());
            ps.setString(3, reservationWaiting.getStatus().name());
            ps.setString(4, reservationWaiting.getCreatedAt().toString());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<ReservationWaiting> findById(Long id) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, reservation_waiting.status, reservation_waiting.created_at, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation_waiting " +
                "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation_waiting.member_id = member.id " +
                "where reservation_waiting.id = ? " +
                "limit 1;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Collection<ReservationWaiting> findAllByMemberIdWithOrder(Long memberId) {
        String sql = "SELECT rw.id, rw.schedule_id, rw.member_id, rw.status, rw.created_at, rw.waiting_num, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from (" +
                "   select *, row_number() over(partition by reservation_waiting.schedule_id order by reservation_waiting.created_at asc) waiting_num " +
                "   from reservation_waiting" +
                "   where reservation_waiting.status = ? " +
                ") rw " +
                "inner join schedule on rw.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on rw.member_id = member.id " +
                "where rw.member_id = ?";

        return jdbcTemplate.query(sql, rowMapperWithWaitingNum, ReservationWaitingStatus.WAITING.name(), memberId);
    }

    public void cancelById(Long id) {
        String sql = "UPDATE reservation_waiting SET status = ? where id = ?;";
        jdbcTemplate.update(sql, ReservationWaitingStatus.CANCELED.name(), id);
    }
}
