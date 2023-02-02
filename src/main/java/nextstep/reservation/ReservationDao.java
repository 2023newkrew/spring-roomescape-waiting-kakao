package nextstep.reservation;

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

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    private final String SELECT_QUERY = "SELECT " +
            "reservation.id, reservation.schedule_id, reservation.member_id, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role ";

    private final String INNER_JOIN_QUERY = "inner join schedule on reservation.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on reservation.member_id = member.id ";

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation.id"),
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
                    .build()
    );

    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, member_id) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setLong(2, reservation.getMember().getId());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = SELECT_QUERY +
                "from reservation " +
                INNER_JOIN_QUERY +
                "where theme.id = ? and schedule.date = ?;";

        return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(date));
    }

    public List<Reservation> findByMemberId(Long id) {
        String sql = SELECT_QUERY +
                "from reservation " +
                INNER_JOIN_QUERY +
                "where member.id = ?;";
        return jdbcTemplate.query(sql, rowMapper, id);
    }

    public Optional<Reservation> findById(Long id) {
        String sql = SELECT_QUERY +
                "from reservation " +
                INNER_JOIN_QUERY +
                "where reservation.id = ?;";

        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findAny();
    }

    public List<Reservation> findByScheduleId(Long id) {
        String sql = SELECT_QUERY +
                "from reservation " +
                INNER_JOIN_QUERY +
                "where schedule.id = ?;";

        return jdbcTemplate.query(sql, rowMapper, id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
