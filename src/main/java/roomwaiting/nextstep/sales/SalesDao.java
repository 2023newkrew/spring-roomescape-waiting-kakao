package roomwaiting.nextstep.sales;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import roomwaiting.nextstep.dbmapper.DatabaseMapper;
import roomwaiting.nextstep.dbmapper.H2Mapper;

import java.util.Optional;

@Component
public class SalesDao {
    private final JdbcTemplate jdbcTemplate;
    private final DatabaseMapper databaseMapper;

    public SalesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.databaseMapper = new H2Mapper();
    }

    public Optional<Long> findAllAmount() {
        String sql = "SELECT sum(sales.price) from sales ";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class));
    }
}
