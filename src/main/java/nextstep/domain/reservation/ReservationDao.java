package nextstep.domain.reservation;

import nextstep.domain.member.Member;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.theme.Theme;
import nextstep.error.ErrorCode;
import nextstep.error.exception.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;


    private static final String RESERVATION_BASE_SQL = "SELECT " +
            "reservation.id, reservation.schedule_id, reservation.member_id, reservation.state, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role " +
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
            ReservationState.valueOf(resultSet.getString("reservation.state"))
    );

    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, member_id, state) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setLong(2, reservation.getMember().getId());
            ps.setString(3, reservation.getState().name());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = RESERVATION_BASE_SQL +
                "where theme.id = ? and schedule.date = ?;";

        return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(date));
    }

    public Reservation findById(Long id) {
        String sql = RESERVATION_BASE_SQL +
                "where reservation.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Reservation> findByScheduleId(Long scheduleId) {
        String sql = RESERVATION_BASE_SQL +
                "where schedule.id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, scheduleId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public List<Reservation> findByMemberId(Long memberId) {
        String sql = RESERVATION_BASE_SQL +
                "where member.id = ?;";

        try {
            return jdbcTemplate.query(sql, rowMapper, memberId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Reservation acceptReservation(Long id) {
        Reservation reservation = findByIdForUpdate(id);

        String updateSql = "UPDATE reservation SET state='ACCEPTED' where id = ?;";
        jdbcTemplate.update(updateSql, id);

        String salesSql = "INSERT INTO sales (reservation_id, refunded) VALUES (?,false);";
        jdbcTemplate.update(salesSql, id);

        return new Reservation(reservation.getId(), reservation.getSchedule(), reservation.getMember(), ReservationState.ACCEPTED);
    }

    public Reservation cancelReservation(Long id) {
        Reservation reservation = findByIdForUpdate(id);
        ReservationState currentState = reservation.getState();

        if (currentState.equals(ReservationState.ACCEPTED)) {
            return cancelForAccepted(reservation);
        }
        if (currentState.equals(ReservationState.UNACCEPTED)) {
            return cancelForUnaccepted(reservation);
        }

        throw new IllegalStateException("취소할 수 없는 예약입니다");
    }

    private Reservation cancelForAccepted(Reservation reservation) {
        String updateSql = "UPDATE reservation SET state='CANCEL_WAITING' where id = ?;";
        jdbcTemplate.update(updateSql, reservation.getId());
        String updateSalesSql = "UPDATE sales SET refunded = true where reservation_id = ?;";
        jdbcTemplate.update(updateSalesSql, reservation.getId());

        return new Reservation(reservation.getId(), reservation.getSchedule(), reservation.getMember(), ReservationState.CANCEL_WAITING);
    }

    private Reservation cancelForUnaccepted(Reservation reservation) {
        String updateSql = "UPDATE reservation SET state='CANCELED' where id = ?;";
        jdbcTemplate.update(updateSql, reservation.getId());

        return new Reservation(reservation.getId(), reservation.getSchedule(), reservation.getMember(), ReservationState.CANCELED);
    }

    private Reservation findByIdForUpdate(Long id) {
        String selectSql = RESERVATION_BASE_SQL + " WHERE reservation.id = ? FOR UPDATE;";

        try {
            Reservation reservation = jdbcTemplate.queryForObject(selectSql, rowMapper, id);

            if (Objects.isNull(reservation))
                throw new EntityNotFoundException(ErrorCode.NO_RESERVATION);

            return reservation;
        } catch (DataAccessException e) {
            throw new EntityNotFoundException(ErrorCode.NO_RESERVATION);
        }
    }

    public Reservation rejectReservation(Long id) {
        Reservation reservation = findByIdForUpdate(id);

        String updateSql = "UPDATE reservation SET state='REJECTED' where id = ?;";
        jdbcTemplate.update(updateSql, reservation.getId());

        if (reservation.getState().equals(ReservationState.ACCEPTED)) {
            String updateSalesSql = "UPDATE sales SET refunded = true where reservation_id = ?;";
            jdbcTemplate.update(updateSalesSql, reservation.getId());
        }

        return new Reservation(reservation.getId(), reservation.getSchedule(), reservation.getMember(), ReservationState.REJECTED);
    }
}
