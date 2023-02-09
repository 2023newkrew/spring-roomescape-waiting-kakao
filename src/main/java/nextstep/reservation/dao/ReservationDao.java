package nextstep.reservation.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.member.Member;
import nextstep.member.Role;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationStatus;
import nextstep.revenue.Revenue;
import nextstep.revenue.RevenueStatus;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class ReservationDao {

    private static final String SELECT_SQL = "SELECT " +
            "reservation.id, reservation.schedule_id, reservation.member_id, reservation.status, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role, " +
            "revenue.id, revenue.amount, revenue.status " +
            "from reservation " +
            "inner join schedule on reservation.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on reservation.member_id = member.id " +
            "left outer join revenue on reservation.revenue_id = revenue.id ";
    private final JdbcTemplate jdbcTemplate;

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
                    Role.valueOf(resultSet.getString("member.role"))
            ),
            resultSet.getLong("revenue.id") == 0 ? null :
                    new Revenue(
                            resultSet.getLong("revenue.id"),
                            resultSet.getInt("revenue.amount"),
                            RevenueStatus.valueOf(resultSet.getString("revenue.status"))
                    ),
            ReservationStatus.valueOf(resultSet.getString("reservation.status"))
    );

    public Long save(Reservation reservation) {
        if (reservation.getId().isEmpty()) {
            return create(reservation);
        }
        return update(reservation);
    }

    public Long create(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, member_id, revenue_id, status) VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setLong(2, reservation.getMember().getId().orElseThrow(() -> {
                throw new RoomReservationException(ErrorCode.INVALID_MEMBER);
            }));
            ps.setObject(3, Objects.isNull(reservation.getRevenue()) ? null : reservation.getRevenue().getId());
            ps.setString(4, reservation.getStatus().toString());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Long update(Reservation reservation) {
        String sql = "UPDATE reservation SET status = ?, revenue_id = ? WHERE id = ?;";

        int updatedCount = jdbcTemplate.update(
                sql,
                reservation.getStatus().toString(),
                Objects.isNull(reservation.getRevenue()) ? null : reservation.getRevenue().getId(),
                reservation.getId().orElseThrow(() -> {
                    throw new RoomReservationException(ErrorCode.INVALID_RESERVATION);
                })
        );
        if (updatedCount != 1) {
            throw new RoomReservationException(ErrorCode.RECORD_NOT_UPDATED);
        }
        return reservation.getId().orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.INVALID_RESERVATION);
        });
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = SELECT_SQL +
                "where theme.id = ? and schedule.date = ?;";

        return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(date));
    }

    public Optional<Reservation> findById(Long id) {
        String sql = SELECT_SQL +
                "where reservation.id = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Reservation> findAllByScheduleId(Long id) {
        String sql = SELECT_SQL +
                "where schedule.id = ?;";
        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Reservation> findValidByScheduleId(Long id) {
        String sql = SELECT_SQL +
                "where schedule.id = ? and reservation.status != ? and reservation.status != ?;";
        try {
            return jdbcTemplate.query(sql, rowMapper, id, ReservationStatus.REFUSED.toString(), ReservationStatus.CANCELLED.toString());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Reservation> findByMemberId(Long id) {
        String sql = SELECT_SQL +
                "where member.id = ?;";

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
}