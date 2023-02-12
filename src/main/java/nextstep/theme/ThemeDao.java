package nextstep.theme;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theme> rowMapper = (resultSet, rowNum) -> Theme.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .desc(resultSet.getString("desc"))
            .price(resultSet.getInt("price"))
            .build();

    public Long save(Theme theme) {
        String sql = "INSERT INTO theme (name, desc, price) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDesc());
            ps.setInt(3, theme.getPrice());
            return ps;

        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * from theme where id = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
    }

    public List<Theme> findAll() {
        String sql = "SELECT * from theme;";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int delete(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        return jdbcTemplate.update(sql, id);
    }
}
