package nextstep.reservation_waiting;

import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
@RequiredArgsConstructor
public class ReservationWaitingDao {

    private final JdbcTemplate jdbcTemplate;

    public Long save(Reservation reservation, Member member) {
        String sql = "INSERT INTO reservation_waiting (schedule_id, member_id) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule()
                    .getId());
            ps.setLong(2, member.getId());
            return ps;

        }, keyHolder);
        return keyHolder.getKey()
                .longValue();
    }
}
