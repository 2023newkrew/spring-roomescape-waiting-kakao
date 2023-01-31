package nextstep.theme;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
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

        return keyHolder.getKey().longValue();
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, desc, price from theme where id = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Theme> findAll() {
        String sql = "SELECT id, name, desc, price from theme;";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
