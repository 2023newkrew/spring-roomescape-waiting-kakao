package com.nextstep.domains.waiting;

import com.nextstep.domains.member.entities.MemberEntity;
import com.nextstep.domains.theme.entities.ThemeEntity;
import com.nextstep.domains.schedule.entities.ScheduleEntity;
import com.nextstep.domains.waiting.entities.WaitingEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

@Component
public class WaitingDao {
    private final JdbcTemplate jdbcTemplate;

    public WaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<WaitingEntity> rowMapper = (resultSet, rowNum) -> new WaitingEntity(
            resultSet.getLong("reservation_waiting.id"),
            new ScheduleEntity(
                    resultSet.getLong("schedule.id"),
                    new ThemeEntity(
                            resultSet.getLong("theme.id"),
                            resultSet.getString("theme.name"),
                            resultSet.getString("theme.desc"),
                            resultSet.getInt("theme.price")
                    ),
                    resultSet.getDate("schedule.date").toLocalDate(),
                    resultSet.getTime("schedule.time").toLocalTime()
            ),
            new MemberEntity(
                    resultSet.getLong("member.id"),
                    resultSet.getString("member.username"),
                    resultSet.getString("member.password"),
                    resultSet.getString("member.name"),
                    resultSet.getString("member.phone"),
                    resultSet.getString("member.role")
            ),
            resultSet.getLong("reservation_waiting.waiting_number")
    );

    public Long save(Long memberId, Long scheduleId) {
        Long waitingNumber = findLastByScheduleId(scheduleId) + 1;
        String sql = "INSERT INTO reservation_waiting (schedule_id, member_id, waiting_number) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, scheduleId);
            ps.setLong(2, memberId);
            ps.setLong(3, waitingNumber);
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Long findLastByScheduleId(Long scheduleId) {
        String sql = "SELECT waiting_number from reservation_waiting where schedule_id = ? order by waiting_number desc";

        return jdbcTemplate.queryForObject(sql, new Object[] { scheduleId }, Long.class);
    }

    public List<WaitingEntity> findByMemberId(Long memberId) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, reservation_waiting.waiting_number " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation_waiting " +
                "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation_waiting.member_id = member.id " +
                "where member.id = ?;";
        try {
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_waiting where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public WaitingEntity findById(Long id) {
        String sql = "SELECT " +
                "reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, reservation_waiting.waiting_number " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation_waiting " +
                "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation_waiting.member_id = member.id " +
                "where reservation_waiting.id = ?;";

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
}
