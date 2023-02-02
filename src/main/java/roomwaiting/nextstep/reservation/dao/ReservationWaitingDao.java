package roomwaiting.nextstep.reservation.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import roomwaiting.nextstep.dbmapper.DatabaseMapper;
import roomwaiting.nextstep.dbmapper.H2Mapper;
import roomwaiting.nextstep.reservation.domain.ReservationWaiting;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class ReservationWaitingDao {

    public final JdbcTemplate jdbcTemplate;
    private final DatabaseMapper databaseMapper;

    public ReservationWaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.databaseMapper = new H2Mapper();
    }

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation_waiting (schedule_id, member_id, wait_num) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setLong(2, reservationWaiting.getMember().getId());
            ps.setLong(3, reservationWaiting.getWaitingNum());
            return ps;

        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<ReservationWaiting> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = SELECT_SQL +
                "where theme.id = ? and schedule.date = ?;";

        return jdbcTemplate.query(sql, databaseMapper.reservationWaitingRowMapper(), themeId, Date.valueOf(date));
    }

    public Optional<ReservationWaiting> findById(Long id) {
        String sql = SELECT_SQL +
                "where reservation_waiting.id = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, databaseMapper.reservationWaitingRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<ReservationWaiting> findByScheduleId(Long id) {
        String sql = SELECT_SQL + "where schedule.id = ?;";
        return jdbcTemplate.query(sql, databaseMapper.reservationWaitingRowMapper(), id);
    }

    public List<ReservationWaiting> findByMemberId(Long id) {
        String sql = SELECT_SQL + "where member.id = ?;";
        return jdbcTemplate.query(sql, databaseMapper.reservationWaitingRowMapper(), id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_waiting where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    private final String SELECT_SQL = "SELECT " +
            "reservation_waiting.id, reservation_waiting.schedule_id, reservation_waiting.member_id, reservation_waiting.wait_num, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.description, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role " +
            "from reservation_waiting " +
            "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on reservation_waiting.member_id = member.id ";
}
