package nextstep.reservationwaiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
public class ReservationWaitingDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationWaitingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationWaiting> rowMapper = (resultSet, rowNum) -> new ReservationWaiting(
            resultSet.getLong("id"),
            resultSet.getLong("schedule_id"),
            resultSet.getLong("member_id"),
            resultSet.getInt("wait_num")
    );

    public Integer getWaitNumByScheduleId(Long id) {
        String sql = "SELECT wait_num FROM RESERVATION_WAITING WHERE schedule_id = ? ORDER BY schedule_id DESC LIMIT 1;";
        try{
            return jdbcTemplate.queryForObject(sql, Integer.class, id);
        } catch (Exception e){
            return 0;
        }
    }

    public Long save(ReservationWaiting reservationWaiting) {
        String sql = "INSERT INTO RESERVATION_WAITING (schedule_id, member_id, wait_num) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getScheduleId());
            ps.setLong(2, reservationWaiting.getMemberId());
            ps.setInt(3, reservationWaiting.getWaitNum());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

//    public Long save(Reservation reservation) {
//        String sql = "INSERT INTO reservation (schedule_id, member_id) VALUES (?, ?);";
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
//            ps.setLong(1, reservation.getSchedule().getId());
//            ps.setLong(2, reservation.getMember().getId());
//            return ps;
//
//        }, keyHolder);
//
//        return keyHolder.getKey().longValue();
//    }
//
//    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
//        String sql = "SELECT " +
//                "reservation.id, reservation.schedule_id, reservation.member_id, " +
//                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
//                "theme.id, theme.name, theme.desc, theme.price, " +
//                "member.id, member.username, member.password, member.name, member.phone, member.role " +
//                "from reservation " +
//                "inner join schedule on reservation.schedule_id = schedule.id " +
//                "inner join theme on schedule.theme_id = theme.id " +
//                "inner join member on reservation.member_id = member.id " +
//                "where theme.id = ? and schedule.date = ?;";
//
//        return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(date));
//    }
//
//    public Reservation findById(Long id) {
//        String sql = "SELECT " +
//                "reservation.id, reservation.schedule_id, reservation.member_id, " +
//                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
//                "theme.id, theme.name, theme.desc, theme.price, " +
//                "member.id, member.username, member.password, member.name, member.phone, member.role " +
//                "from reservation " +
//                "inner join schedule on reservation.schedule_id = schedule.id " +
//                "inner join theme on schedule.theme_id = theme.id " +
//                "inner join member on reservation.member_id = member.id " +
//                "where reservation.id = ?;";
//        try {
//            return jdbcTemplate.queryForObject(sql, rowMapper, id);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public List<Reservation> findByScheduleId(Long id) {
//        String sql = "SELECT " +
//                "reservation.id, reservation.schedule_id, reservation.member_id, " +
//                "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
//                "theme.id, theme.name, theme.desc, theme.price, " +
//                "member.id, member.username, member.password, member.name, member.phone, member.role " +
//                "from reservation " +
//                "inner join schedule on reservation.schedule_id = schedule.id " +
//                "inner join theme on schedule.theme_id = theme.id " +
//                "inner join member on reservation.member_id = member.id " +
//                "where schedule.id = ?;";
//
//        try {
//            return jdbcTemplate.query(sql, rowMapper, id);
//        } catch (Exception e) {
//            return Collections.emptyList();
//        }
//    }
//
//    public void deleteById(Long id) {
//        String sql = "DELETE FROM reservation where id = ?;";
//        jdbcTemplate.update(sql, id);
//    }
}
