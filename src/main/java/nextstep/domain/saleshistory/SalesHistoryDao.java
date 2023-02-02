package nextstep.domain.saleshistory;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class SalesHistoryDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<SalesHistory> rowMapper = (resultSet, rowNum) -> new SalesHistory(
            resultSet.getLong("id"),
            resultSet.getLong("theme_id"),
            resultSet.getInt("amount"),
            resultSet.getString("status")
    );

    public void save(SalesHistory salesHistory) {
        String sql = "INSERT INTO sales_history (theme_id, amount, status, created_at) VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(sql, salesHistory.getThemeId(), salesHistory.getAmount(), salesHistory.getStatus(), salesHistory.getCreatedAt());
    }

    public List<SalesHistory> findHistoryByCreatedAt(Long lastHistoryId, LocalDateTime yesterday, int size) {
        String sql = Objects.nonNull(lastHistoryId)
                ? "SELECT * FROM sales_history WHERE id < ? AND created_at >= ? LIMIT ?"
                : "SELECT * FROM sales_history WHERE created_at >= ? LIMIT ?";
        Object[] args = Objects.nonNull(lastHistoryId)
                ? new Object[]{ lastHistoryId, yesterday, size }
                : new Object[]{ yesterday, size };

        try {
            return jdbcTemplate.query(sql, rowMapper, args);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

}
