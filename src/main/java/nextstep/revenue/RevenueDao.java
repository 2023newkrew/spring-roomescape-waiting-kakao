package nextstep.revenue;

import nextstep.reservation.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class RevenueDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Revenue> rowMapper = (resultSet, rowNum) -> new Revenue(
            resultSet.getLong("revenue.id"),
            resultSet.getLong("reservation.id"),
            resultSet.getLong("revenue.price")
    );

    public RevenueDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Revenue revenue) {
        String sql = "INSERT INTO revenue (reservation_id, price) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setLong(1, revenue.getReservationId());
            ps.setLong(2, revenue.getPrice());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
