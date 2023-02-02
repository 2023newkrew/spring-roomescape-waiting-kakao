package app.nextstep.dao;

import app.nextstep.entity.MemberEntity;
import app.nextstep.entity.ReservationEntity;
import app.nextstep.entity.ScheduleEntity;
import app.nextstep.entity.ThemeEntity;
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

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationEntity> rowMapper = (resultSet, rowNum) -> new ReservationEntity(
            resultSet.getLong("reservation.id"),
            new ScheduleEntity(
                    resultSet.getLong("schedule.id"),
                    new ThemeEntity(
                            resultSet.getLong("theme.id"),
                            resultSet.getString("theme.name"),
                            resultSet.getString("theme.desc"),
                            resultSet.getInt("theme.price")),
                    resultSet.getDate("schedule.date"),
                    resultSet.getTime("schedule.time")),
            new MemberEntity(
                    resultSet.getLong("member.id"),
                    resultSet.getString("member.username"),
                    resultSet.getString("member.password"),
                    resultSet.getString("member.role"),
                    resultSet.getString("member.name"),
                    resultSet.getString("member.phone")));

    public ReservationEntity findById(Long id) {
        String sql = "SELECT * FROM reservation " +
                "JOIN schedule ON reservation.schedule_id = schedule.id " +
                "JOIN theme ON schedule.theme_id = theme.id " +
                "JOIN member ON reservation.member_id = member.id " +
                "WHERE reservation.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReservationEntity> findByThemeIdAndDate(Long themeId, Date date) {
        String sql = "SELECT * FROM reservation " +
                "JOIN schedule ON reservation.schedule_id = schedule.id " +
                "JOIN theme ON schedule.theme_id = theme.id " +
                "JOIN member ON reservation.member_id = member.id " +
                "WHERE theme.id = ? AND schedule.date = ?;";

        return jdbcTemplate.query(sql, rowMapper, themeId, date);
    }

    public List<ReservationEntity> findByScheduleId(Long scheduleId) {
        String sql = "SELECT * FROM reservation " +
                "JOIN schedule ON reservation.schedule_id = schedule.id " +
                "JOIN theme ON schedule.theme_id = theme.id " +
                "JOIN member ON reservation.member_id = member.id " +
                "WHERE schedule.id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, scheduleId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Long save(Long scheduleId, Long memberId) {
        String sql = "INSERT INTO reservation (schedule_id, member_id) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, scheduleId);
            ps.setLong(2, memberId);
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
