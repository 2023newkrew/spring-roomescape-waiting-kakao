package nextstep.reservation.dao;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.model.Reservation;
import nextstep.schedule.model.Schedule;
import nextstep.theme.model.Theme;
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

    public final JdbcTemplate jdbcTemplate;

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
            resultSet.getString("reservation.member_name")
    );

    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, member_name) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setString(2, reservation.getMemberName());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findByMemberId(Long memberId){
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public List<Reservation> findAllByThemeIdAndDateAndMemberName(Long themeId, String date, String memberName) {
        String sql = "SELECT reservation.id, reservation.schedule_id, reservation.member_name, schedule.id, schedule.theme_id, schedule.date, schedule.time, theme.id, theme.name, theme.desc, theme.price " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "where theme.id = ? and schedule.date = ? and reservation.member_name = ?";

        return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(date), memberName);
    }

    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT reservation.id, reservation.schedule_id, reservation.member_name, schedule.id, schedule.theme_id, schedule.date, schedule.time, theme.id, theme.name, theme.desc, theme.price " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "where reservation.id = ?;";

        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    public List<Reservation> findByScheduleId(Long id) {
        String sql = "SELECT reservation.id, reservation.schedule_id, reservation.member_name, schedule.id, schedule.theme_id, schedule.date, schedule.time, theme.id, theme.name, theme.desc, theme.price " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "where schedule.id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
