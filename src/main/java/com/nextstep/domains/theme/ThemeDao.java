package com.nextstep.domains.theme;

import com.nextstep.domains.theme.entities.ThemeEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class ThemeDao {
    private JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ThemeEntity> rowMapper = (resultSet, rowNum) -> new ThemeEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("desc"),
            resultSet.getInt("price")
    );

    public Long save(ThemeEntity theme) {
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

    public ThemeEntity findById(Long id) {
        String sql = "SELECT id, name, desc, price from theme where id = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<ThemeEntity> findAll() {
        String sql = "SELECT id, name, desc, price from theme;";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
