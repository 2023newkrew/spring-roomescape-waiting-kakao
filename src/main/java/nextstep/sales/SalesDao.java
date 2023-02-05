package nextstep.sales;

import nextstep.reservation.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class SalesDao {

    private JdbcTemplate jdbcTemplate;

    public SalesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Long reservationId, int amount) {
        String sql = "INSERT INTO sales (reservation_id, amount) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationId);
            ps.setInt(2, amount);
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

}
