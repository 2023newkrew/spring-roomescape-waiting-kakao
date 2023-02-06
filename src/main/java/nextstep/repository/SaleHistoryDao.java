package nextstep.repository;

import nextstep.domain.SaleHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.util.Objects;

@Component
public class SaleHistoryDao {
    private final JdbcTemplate jdbcTemplate;

    public SaleHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long save(SaleHistory saleHistory) {
        String sql = "INSERT INTO sale_history " +
                "(theme_name, theme_price, schedule_date, schedule_time, member_username, member_phone, reservation_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, saleHistory.getThemeName());
            ps.setInt(2, saleHistory.getThemePrice());
            ps.setDate(3, Date.valueOf(saleHistory.getScheduleDate()));
            ps.setTime(4, Time.valueOf(saleHistory.getScheduleTime()));
            ps.setString(5, saleHistory.getMemberUsername());
            ps.setString(6, saleHistory.getMemberPhone());
            ps.setLong(7, saleHistory.getReservationId());
            return ps;

        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM sale_history where id = ?;", id);
    }
}
