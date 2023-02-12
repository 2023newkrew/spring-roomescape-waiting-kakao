package nextstep.revenue;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class RevenueDao {

    private static final String SELECT_SQL = "select revenue.id, revenue.amount, revenue.status, " +
            "from revenue ";
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Revenue> rowMapper = (resultSet, rowNum) -> new Revenue(
            resultSet.getLong("revenue.id"),
            resultSet.getInt("revenue.amount"),
            RevenueStatus.valueOf(resultSet.getString("revenue.status"))
    );

    public RevenueDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long save(Revenue revenue) {
        if (revenue.getId().isEmpty()) {
            return create(revenue);
        }
        return update(revenue);
    }

    public long create(Revenue revenue) {
        String sql = "insert into revenue (amount, status) values(?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, revenue.getAmount());
            ps.setString(2, revenue.getStatus().toString());
            return ps;
        }, keyHolder);
        long savedId = keyHolder.getKey().longValue();
        revenue.setId(savedId);
        return savedId;
    }

    public long update(Revenue revenue) {
        String sql = "update revenue set status = ? where id = ?;";
        int updatedCount = jdbcTemplate.update(sql, revenue.getStatus().toString(), revenue.getId().orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.INVALID_REVENUE);
        }));
        if (updatedCount != 1) {
            throw new RoomReservationException(ErrorCode.RECORD_NOT_UPDATED);
        }
        return revenue.getId().orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.INVALID_REVENUE);
        });
    }

    public List<Revenue> findAll() {
        String sql = SELECT_SQL + ";";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Revenue> findById(Long id) {
        String sql = SELECT_SQL
                + "where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
