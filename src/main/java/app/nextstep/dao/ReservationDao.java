package app.nextstep.dao;

import app.nextstep.entity.*;
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

    private final RowMapper<ReservationEntity> reservationEntityRowMapper = (resultSet, rowNum) -> new ReservationEntity(
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
                    resultSet.getString("member.phone")),
            resultSet.getString("reservation.status"));

    private final RowMapper<ReservationWaitingEntity> reservationWaitingEntityRowMapper = (resultSet, rowNum) -> new ReservationWaitingEntity(
            resultSet.getLong("reservation_id"),
            new ScheduleEntity(
                    resultSet.getLong("schedule_id"),
                    new ThemeEntity(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_desc"),
                            resultSet.getInt("theme_price")),
                    resultSet.getDate("schedule_date"),
                    resultSet.getTime("schedule_time")),
            new MemberEntity(
                    resultSet.getLong("member_id"),
                    resultSet.getString("member_username"),
                    resultSet.getString("member_password"),
                    resultSet.getString("member_role"),
                    resultSet.getString("member_name"),
                    resultSet.getString("member_phone")),
            resultSet.getLong("waiting_number"));

    public ReservationEntity findById(Long id) {
        String sql = "SELECT * FROM reservation " +
                "JOIN schedule ON reservation.schedule_id = schedule.id " +
                "JOIN theme ON schedule.theme_id = theme.id " +
                "JOIN member ON reservation.member_id = member.id " +
                "WHERE reservation.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, reservationEntityRowMapper, id);
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

        return jdbcTemplate.query(sql, reservationEntityRowMapper, themeId, date);
    }

    public List<ReservationEntity> findByScheduleId(Long scheduleId) {
        String sql = "SELECT * FROM reservation " +
                "JOIN schedule ON reservation.schedule_id = schedule.id " +
                "JOIN theme ON schedule.theme_id = theme.id " +
                "JOIN member ON reservation.member_id = member.id " +
                "WHERE schedule.id = ?;";

        try {
            return jdbcTemplate.query(sql, reservationEntityRowMapper, scheduleId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<ReservationEntity> findConfirmedByMemberId(Long memberId) {
        String sql = "SELECT * FROM reservation " +
                "JOIN schedule ON reservation.schedule_id = schedule.id " +
                "JOIN theme ON schedule.theme_id = theme.id " +
                "JOIN member ON reservation.member_id = member.id " +
                "WHERE member.id = ? AND reservation.status = 'CONFIRMED';";
        try {
            return jdbcTemplate.query(sql, reservationEntityRowMapper, memberId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReservationWaitingEntity> findWaitingByMemberId(Long memberId) {
        String sql = "SELECT * FROM (SELECT RANK() OVER (PARTITION BY schedule.id ORDER BY reservation.id) as waiting_number, " +
                            "reservation.id reservation_id, reservation.status reservation_status, " +
                            "schedule.id schedule_id, schedule.date schedule_date, schedule.time schedule_time, " +
                            "theme.id theme_id, theme.name theme_name, theme.desc theme_desc, theme.price theme_price, " +
                            "member.id member_id, member.username member_username, member.password member_password, " +
                            "member.role member_role, member.name member_name, member.phone member_phone " +
                        "FROM reservation " +
                        "JOIN schedule ON reservation.schedule_id = schedule.id " +
                        "JOIN theme ON schedule.theme_id = theme.id " +
                        "JOIN member ON reservation.member_id = member.id " +
                        "WHERE reservation.status = 'WAITING') r " +
                "WHERE member_id = ?;";
        try {
            return jdbcTemplate.query(sql, reservationWaitingEntityRowMapper, memberId);
        } catch (Exception e) {
            return null;
        }
    }

    public Long save(Long scheduleId, Long memberId, String status) {
        String sql = "INSERT INTO reservation (schedule_id, member_id, status) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, scheduleId);
            ps.setLong(2, memberId);
            ps.setString(3, status);
            return ps;

        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
