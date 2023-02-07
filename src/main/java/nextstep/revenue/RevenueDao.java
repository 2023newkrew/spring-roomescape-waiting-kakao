package nextstep.revenue;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Objects;

@Component
public class RevenueDao {

    private final JdbcTemplate jdbcTemplate;

    public RevenueDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Revenue revenue) {
        String sql = "INSERT INTO revenue (reservation_id, price) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, revenue.getReservationId());
            ps.setInt(2, revenue.getPrice());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteById(Long reservationId) {
        String sql = "DELETE FROM revenue WHERE reservationId = ?;";
        jdbcTemplate.update(sql, reservationId);
    }
}
