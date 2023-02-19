package app.nextstep.dao;

import app.nextstep.entity.MemberEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;

@Component
public class MemberDao {
    public final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberEntity> rowMapper = (resultSet, rowNum) -> new MemberEntity(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("role"),
            resultSet.getString("name"),
            resultSet.getString("phone")
    );

    public MemberEntity findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public MemberEntity findByUsername(String username) {
        String sql = "SELECT *FROM member WHERE username = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }

    public Long save(String username, String password, String role, String name, String phone) {
        String sql = "INSERT INTO member (username, password, role, name, phone) VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.setString(4, name);
            ps.setString(5, phone);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
