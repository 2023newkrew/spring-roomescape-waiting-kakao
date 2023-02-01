package app.auth.dao;

import app.auth.domain.UserDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao {
    public final JdbcTemplate jdbcTemplate;

    public LoginDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserDetail> rowMapper = (resultSet, rowNum) -> new UserDetail(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("role")
    );

    public UserDetail findById(Long id) {
        String sql = "SELECT id, username, password, role from member where id = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public UserDetail findByUsername(String username) {
        String sql = "SELECT id, username, password, role from member where username = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }
}
