package nextstep.domain.saleshistory;

import lombok.RequiredArgsConstructor;
import nextstep.domain.reservation.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

@RequiredArgsConstructor
@Repository
public class SalesHistoryDao {

    private final JdbcTemplate jdbcTemplate;

    public void save(SalesHistory salesHistory) {
        String sql = "INSERT INTO sales_history (theme_id, amount, status, created_at) VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(sql, salesHistory.getThemeId(), salesHistory.getAmount(), salesHistory.getStatus(), salesHistory.getCreatedAt());
    }

}
