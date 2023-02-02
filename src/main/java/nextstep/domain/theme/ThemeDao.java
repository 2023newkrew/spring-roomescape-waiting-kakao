package nextstep.domain.theme;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theme> rowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("desc"),
            resultSet.getInt("price")
    );

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
        try {
            String sql = "SELECT id, name, desc, price from theme where id = ?;";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Boolean existsById(Long id) {
        String sql = "SELECT 1 FROM theme WHERE id = ?;";
        return jdbcTemplate.query(sql, ResultSet::next, id);
    }

    public List<Theme> findAll() {
        try {
            String sql = "SELECT id, name, desc, price from theme;";
            return jdbcTemplate.query(sql, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM theme where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public List<Theme> findByIds(List<Long> ids) {
        try {
            String sql = String.format("SELECT name FROM theme WHERE id IN(%s)", String.join(",", Collections.nCopies(ids.size(), "?")));
            return jdbcTemplate.query(sql, rowMapper, ids.toArray());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }
}
