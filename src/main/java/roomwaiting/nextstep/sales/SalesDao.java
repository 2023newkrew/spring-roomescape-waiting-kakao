package roomwaiting.nextstep.sales;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomwaiting.nextstep.dbmapper.DatabaseMapper;
import roomwaiting.nextstep.dbmapper.H2Mapper;

import java.sql.PreparedStatement;
import java.util.List;
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

    public void updateSales(Sales sales) {
        String sql = "INSERT INTO sales (member_username, price, schedule_id) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, sales.getMember().getUsername());
            ps.setLong(2, sales.getPrice());
            ps.setLong(3, sales.getSchedule().getId());
            return ps;
        }, keyHolder);
    }

    public List<Sales> findAllSales() {
        return jdbcTemplate.query(SELECT_SQL, databaseMapper.salesRowMapper());
    }

    public List<Sales> findByMemberId(Long memberId) {
        String sql = SELECT_SQL + "where member.id = ?";
        return jdbcTemplate.query(sql, databaseMapper.salesRowMapper(), memberId);
    }


    private final String SELECT_SQL = "SELECT " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.description, theme.price, " +
            "member.id, member.username, member.password, member.name, member.phone, member.role, " +
            "sales.id, sales.price " +
            "from sales " +
            "inner join schedule on sales.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "inner join member on sales.member_username = member.username ";
}
