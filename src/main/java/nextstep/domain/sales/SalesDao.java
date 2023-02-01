package nextstep.domain.sales;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;

public class SalesDao {

    public final JdbcTemplate jdbcTemplate;

    public SalesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Sales sales) {
        String sql = "INSERT INTO sales (reservation_id, refunded) values (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();


        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, sales.getReservation().getId());
            ps.setBoolean(2, sales.isRefunded());

            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
