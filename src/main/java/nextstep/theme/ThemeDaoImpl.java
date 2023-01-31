package nextstep.theme;

import static nextstep.theme.ThemeJdbcSql.DELETE_BY_ID;
import static nextstep.theme.ThemeJdbcSql.INSERT_INTO_STATEMENT;
import static nextstep.theme.ThemeJdbcSql.SELECT_ALL_STATEMENT;
import static nextstep.theme.ThemeJdbcSql.SELECT_BY_ID_STATEMENT;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ThemeDaoImpl implements ThemeDao {
    private final JdbcTemplate jdbcTemplate;

    public ThemeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theme> rowMapper = (resultSet, rowNum) -> Theme.giveId(Theme.builder()
            .name(resultSet.getString("name"))
            .desc(resultSet.getString("desc"))
            .price(resultSet.getInt("price"))
            .build(), resultSet.getLong("id"));

    @Override
    public Long save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_INTO_STATEMENT, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDesc());
            ps.setInt(3, theme.getPrice());
            return ps;

        }, keyHolder);

        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public Theme findById(Long id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID_STATEMENT, rowMapper, id);
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(SELECT_ALL_STATEMENT, rowMapper);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
