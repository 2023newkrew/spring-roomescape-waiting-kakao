package nextstep.revenue;

import nextstep.reservation.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;

@Component
public class RevenueDao {
    public final JdbcTemplate jdbcTemplate;

    public RevenueDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Revenue revenue) {
        String sql = "INSERT INTO revenue (reservation_id, amount, date) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, revenue.getReservation().getId());
            ps.setInt(2, revenue.getAmount());
            ps.setDate(3, Date.valueOf(revenue.getDate()));
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
