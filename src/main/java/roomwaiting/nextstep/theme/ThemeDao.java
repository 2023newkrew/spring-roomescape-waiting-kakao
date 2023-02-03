package roomwaiting.nextstep.theme;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomwaiting.nextstep.dbmapper.DatabaseMapper;
import roomwaiting.nextstep.dbmapper.H2Mapper;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final DatabaseMapper databaseMapper;


    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.databaseMapper = new H2Mapper();
    }

    public Long save(Theme theme) {
        String sql = "INSERT INTO theme (name, description, price) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setLong(3, theme.getPrice());
            return ps;

        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description, price from theme where id = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, databaseMapper.themeRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, price from theme;";
        return jdbcTemplate.query(sql, databaseMapper.themeRowMapper());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM theme where id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public Boolean isExistsByNameAndPrice(ThemeRequest theme){
        String sql = "SELECT EXISTS(SELECT 1 FROM theme WHERE name=? and price=?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, theme.getName(), theme.getPrice());
    }
}
