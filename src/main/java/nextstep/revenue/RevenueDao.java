package nextstep.revenue;

import nextstep.theme.Theme;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

@Component
public class RevenueDao {

    private final JdbcTemplate jdbcTemplate;

    public RevenueDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Revenue> rowMapper = (rs, rowNum) -> Revenue.builder()
            .id(rs.getLong("id"))
            .reservationId(rs.getLong("reservation_id"))
            .price(rs.getInt("price"))
            .build();

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

    public Optional<Revenue> findByReservationId(Long reservationId) {
        String sql = "SELECT * FROM revenue WHERE reservation_id = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, reservationId));
    }

    public void deleteById(Long reservationId) {
        String sql = "DELETE FROM revenue WHERE reservation_id = ?;";
        jdbcTemplate.update(sql, reservationId);
    }
}
