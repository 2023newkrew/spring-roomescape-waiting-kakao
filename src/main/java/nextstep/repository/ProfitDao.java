package nextstep.repository;

import lombok.RequiredArgsConstructor;
import nextstep.domain.persist.Profit;
import nextstep.domain.persist.Reservation;
import nextstep.domain.persist.Schedule;
import nextstep.domain.persist.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfitDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Profit> rowMapper = (resultSet, rowNum) -> new Profit(
            resultSet.getLong("id"),
            resultSet.getTimestamp("occurrence_date").toLocalDateTime(),
            resultSet.getInt("amount")
    );

    public List<Profit> findAll() {
        String sql = "SELECT * FROM profit";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Long save(LocalDateTime localDateTime, int amount) {
        String sql = "INSERT INTO profit (occurrence_date, amount) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setTimestamp(1, Timestamp.valueOf(localDateTime));
            ps.setInt(2, amount);
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
