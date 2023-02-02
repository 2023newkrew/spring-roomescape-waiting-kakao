package nextstep.reservationwaitings;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationWaitingNumGenerator {

    private final JdbcTemplate jdbcTemplate;

    public ReservationWaitingNumGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long getWaitNum(long scheduleId) {
        Long waitNum = jdbcTemplate.queryForObject(
                "SELECT wait_num FROM reservation_waiting_num WHERE schedule_id = ? FOR UPDATE", Long.class, scheduleId
        );
        jdbcTemplate.update("UPDATE reservation_waiting_num SET wait_num = wait_num + 1 WHERE schedule_id = ?", scheduleId);
        return waitNum;
    }

    public void createWaitNum(long scheduleId) {
        jdbcTemplate.update("INSERT INTO reservation_waiting_num(schedule_id, wait_num) VALUES(?, 1)", scheduleId);
    }
}
