package nextstep.waiting;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class ReservationWaitingDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationWaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationWaiting> rowMapper = (resultSet, rowNum) -> new ReservationWaiting(
            new Reservation(
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
                    )),
            resultSet.getLong("reservation.waiting_seq")
    );

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO reservation (schedule_id, member_id, waiting_seq) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getReservation().getSchedule().getId());
            ps.setLong(2, reservationWaiting.getReservation().getMember().getId());
            ps.setLong(3, reservationWaiting.getWaitingSeq());
            return ps;
        }, keyHolder);

        return keyHolder.getKeyAs(Long.class);
    }

    public Optional<ReservationWaiting> findById(Long id) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, reservation.waiting_seq, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where reservation.id = ? and reservation.waiting_seq > 0;";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<ReservationWaiting> findByMemberId(Long memberId) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, reservation.waiting_seq, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where member.id = ? and reservation.waiting_seq > 0;";

            return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public List<ReservationWaiting> findByScheduleId(Long id) {
        String sql = "SELECT " +
                "reservation.id, reservation.schedule_id, reservation.member_id, reservation.waiting_seq, " +
                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
                "theme.id, theme.name, theme.desc, theme.price, " +
                "member.id, member.username, member.password, member.name, member.phone, member.role " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where schedule.id = ? and reservation.waiting_seq > 0;";

            return jdbcTemplate.query(sql, rowMapper, id);
    }

    public void updateWaitingSeq(Long reservationId, Long waitingSeq) {
        String sql = "UPDATE reservation SET waiting_seq = ? where id = ?";
        jdbcTemplate.update(sql, waitingSeq, reservationId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ? and waiting_seq > 0;";
        jdbcTemplate.update(sql, id);
    }
}
