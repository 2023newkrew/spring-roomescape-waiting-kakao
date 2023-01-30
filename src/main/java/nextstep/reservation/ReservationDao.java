package nextstep.reservation;

import auth.Role;
import java.util.Optional;
import javax.sql.DataSource;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

@Component
public class ReservationDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> Reservation.builder()
            .id(resultSet.getLong("reservation.id"))
            .schedule(Schedule.builder()
                    .id(resultSet.getLong("schedule.id"))
                    .theme(Theme.builder()
                            .id(resultSet.getLong("theme.id"))
                            .name(resultSet.getString("theme.name"))
                            .desc(resultSet.getString("theme.desc"))
                            .price(resultSet.getInt("theme.price"))
                            .build())
                    .date(resultSet.getDate("schedule.date").toLocalDate())
                    .time(resultSet.getTime("schedule.time").toLocalTime())
                    .build())
            .member(Member.builder()
                    .id(resultSet.getLong("member.id"))
                    .username(resultSet.getString("member.username"))
                    .password(resultSet.getString("member.password"))
                    .name(resultSet.getString("member.name"))
                    .phone(resultSet.getString("member.phone"))
                    .role(Role.valueOf(resultSet.getString("member.role")))
                    .build())
            .build();

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
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where theme.id = ? and schedule.date = ?;";

        return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(date));
    }

    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where reservation.id = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Reservation> findByScheduleId(Long id) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where schedule.id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public List<Reservation> findByMemberId(Long id) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where member.id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
