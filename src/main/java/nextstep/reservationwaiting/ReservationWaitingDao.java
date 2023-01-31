package nextstep.reservationwaiting;

import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.Role;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReservationWaitingDao {
    private final JdbcTemplate jdbcTemplate;

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
            Member.builder()
                    .id(resultSet.getLong("member.id"))
                    .username(resultSet.getString("member.username"))
                    .password(resultSet.getString("member.password"))
                    .name(resultSet.getString("member.name"))
                    .phone(resultSet.getString("member.phone"))
                    .role(Role.valueOf(resultSet.getString("member.role")))
                    .build(),
            resultSet.getLong("wait_num")
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation_waiting (schedule_id, member_id) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setLong(2, reservationWaiting.getMember().getId());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<ReservationWaiting> findByMemberId(Long memberId) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role, " +
                "wait_num " +
                "FROM (" +
                "    SELECT " +
                "    id, schedule_id, member_id, " +
                "    ROW_NUMBER() OVER(PARTITION BY schedule_id ORDER BY id) AS wait_num " +
                "    FROM reservation_waiting" +
                ") AS reservation_waiting " +
                "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation_waiting.member_id = member.id " +
                "WHERE member_id = ?;";

        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<ReservationWaiting> findById(Long id) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role," +
                "0 AS wait_num " +
                "from reservation_waiting " +
                "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation_waiting.member_id = member.id " +
                "where reservation_waiting.id = ?;";

        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findAny();
    }

    public Optional<ReservationWaiting> findMinIdByScheduleId(Schedule schedule) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role," +
                "0 AS wait_num " +
                "from reservation_waiting " +
                "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation_waiting.member_id = member.id " +
                "WHERE reservation_waiting.id = (" +
                "    SELECT " +
                "    MIN(id) " +
                "    FROM reservation_waiting " +
                "    WHERE schedule_id = ?" +
                ")";

        return jdbcTemplate.query(sql, rowMapper, schedule.getId())
                .stream()
                .findAny();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_waiting where id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
