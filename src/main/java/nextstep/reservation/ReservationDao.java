package nextstep.reservation;

import java.util.Optional;
import nextstep.exceptions.exception.DuplicatedReservationException;
import nextstep.reservation_waiting.ReservationWaiting;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

import static nextstep.utils.RowMapperUtil.reservationRowMapper;
import static nextstep.utils.RowMapperUtil.reservationWaitingRowMapper;

@Component
public class ReservationDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
        String sql = "select *\n" +
                "from RESERVATION,\n" +
                "     (SELECT R.id RESERVATION_ID,\n" +
                "             ROW_NUMBER() OVER (PARTITION BY R.SCHEDULE_ID ORDER BY R.CREATED_DATETIME) WAIT_NUM\n" +
                "      FROM RESERVATION R)\n" +
                "         JOIN SCHEDULE ON RESERVATION.SCHEDULE_ID = SCHEDULE.ID\n" +
                "         JOIN THEME ON SCHEDULE.THEME_ID = THEME.ID\n" +
                "         JOIN MEMBER ON RESERVATION.MEMBER_ID = MEMBER.ID\n" +
                "where RESERVATION.id = RESERVATION_ID\n" +
                "  and WAIT_NUM = 1\n" +
                "  and THEME.id = (?)\n" +
                "  and SCHEDULE.DATE = (?)";

        return jdbcTemplate.query(sql, reservationRowMapper, themeId, Date.valueOf(date));
    }

    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT * " +
                "from reservation " +
                "inner join schedule on reservation.schedule_id = schedule.id " +
                "inner join theme on schedule.theme_id = theme.id " +
                "inner join member on reservation.member_id = member.id " +
                "where reservation.id = ?";

        return jdbcTemplate.query(sql, reservationRowMapper, id).stream().findAny();
    }

    public Optional<Reservation> findByScheduleId(Long id) {
        String sql = "select *\n" +
                "from RESERVATION,\n" +
                "     (SELECT R.id RESERVATION_ID,\n" +
                "             ROW_NUMBER() OVER (PARTITION BY R.SCHEDULE_ID ORDER BY R.CREATED_DATETIME) WAIT_NUM\n" +
                "      FROM RESERVATION R)\n" +
                "         JOIN SCHEDULE ON RESERVATION.SCHEDULE_ID = SCHEDULE.ID\n" +
                "         JOIN THEME ON SCHEDULE.THEME_ID = THEME.ID\n" +
                "         JOIN MEMBER ON RESERVATION.MEMBER_ID = MEMBER.ID\n" +
                "where RESERVATION.id = RESERVATION_ID\n" +
                "  and WAIT_NUM = 1\n" +
                "  and SCHEDULE_ID = (?);";
        return jdbcTemplate.query(sql, reservationRowMapper, id).stream().findAny();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public List<Reservation> findAllByMemberId(Long memberId) {
        String sql = "select *\n" +
                "from RESERVATION,\n" +
                "     (SELECT R.id RESERVATION_ID,\n" +
                "             ROW_NUMBER() OVER (PARTITION BY R.SCHEDULE_ID ORDER BY R.CREATED_DATETIME) WAIT_NUM\n" +
                "      FROM RESERVATION R)\n" +
                "         JOIN SCHEDULE ON RESERVATION.SCHEDULE_ID = SCHEDULE.ID\n" +
                "         JOIN THEME ON SCHEDULE.THEME_ID = THEME.ID\n" +
                "         JOIN MEMBER ON RESERVATION.MEMBER_ID = MEMBER.ID\n" +
                "where RESERVATION.id = RESERVATION_ID\n" +
                "  and WAIT_NUM = 1\n" +
                "  and MEMBER_ID = (?);";
        return jdbcTemplate.query(sql, reservationRowMapper, memberId);
    }

    public List<ReservationWaiting> findAllWaitingByMemberId(Long memberId) {
        String sql = "select *\n" +
                "from RESERVATION,\n" +
                "     (SELECT R.id RESERVATION_ID,\n" +
                "             ROW_NUMBER() OVER (PARTITION BY R.SCHEDULE_ID ORDER BY R.CREATED_DATETIME) WAIT_NUM\n" +
                "      FROM RESERVATION R)\n" +
                "         JOIN SCHEDULE ON RESERVATION.SCHEDULE_ID = SCHEDULE.ID\n" +
                "         JOIN THEME ON SCHEDULE.THEME_ID = THEME.ID\n" +
                "         JOIN MEMBER ON RESERVATION.MEMBER_ID = MEMBER.ID\n" +
                "where RESERVATION.id = RESERVATION_ID\n" +
                "  and WAIT_NUM > 1\n" +
                "  and MEMBER_ID = (?);";

        return jdbcTemplate.query(sql, reservationWaitingRowMapper, memberId);
    }

}
