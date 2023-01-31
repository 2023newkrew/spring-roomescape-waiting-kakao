package nextstep.reservation;

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
    private static final String DEFAULT_SELECT_FROM_SQL =
            "SELECT " +
            "reservation.id, reservation.schedule_id, reservation.member_id, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role, " +
            "reservation.wait_num " +
            "from reservation " +
            "inner join schedule on reservation.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on reservation.member_id = member.id ";

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
            new Member(
                    resultSet.getLong("member.id"),
                    resultSet.getString("member.username"),
                    resultSet.getString("member.password"),
                    resultSet.getString("member.name"),
                    resultSet.getString("member.phone"),
                    resultSet.getString("member.role")
            ),
            resultSet.getLong("reservation.wait_num")
    );

    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, member_id, wait_num) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setLong(2, reservation.getMember().getId());
            ps.setLong(3, reservation.getWaitNum());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = DEFAULT_SELECT_FROM_SQL +
                "where theme.id = ? and schedule.date = ? and reservation.wait_num = 0;";

        return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(date));
    }

    public Reservation findById(Long id) {
        String sql = DEFAULT_SELECT_FROM_SQL +
                "where reservation.id = ? and reservation.wait_num = 0;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public Reservation findWaitingById(Long id) {
        String sql = DEFAULT_SELECT_FROM_SQL +
                "where reservation.id = ? and reservation.wait_num > 0;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Reservation> findByScheduleId(Long id) {
        String sql = DEFAULT_SELECT_FROM_SQL +
                "where schedule.id = ?;";
        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Reservation> findAllByMemberId(Long id) {
        String sql = DEFAULT_SELECT_FROM_SQL +
                "where member.id = ? and reservation.wait_num = 0;";
        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Reservation> findAllWaitingByMemberId(Long id) {
        String sql = DEFAULT_SELECT_FROM_SQL +
                "where member.id = ? and reservation.wait_num > 0;";
        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ? and reservation.wait_num = 0;";
        jdbcTemplate.update(sql, id);
    }

    public void deleteWaitingById(Long id) {
        String sql = "DELETE FROM reservation where id = ? and reservation.wait_num > 0;";
        jdbcTemplate.update(sql, id);
    }

    public void adjustWaitNumByScheduleIdAndBaseNum(Long scheduleId, Long baseWaitNum) {
        String sql = "UPDATE reservation set wait_num = wait_num - 1 where schedule_id = ? and wait_num > ?";
        jdbcTemplate.update(sql, scheduleId, baseWaitNum);
    }
}
