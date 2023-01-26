package app.auth;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    public Optional<UserDetail> findById(Long id) {
        String sql = "SELECT id, username, password, role, role from member where id = ?;";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    public Optional<UserDetail> findByUsername(String username) {
        String sql = "SELECT id, username, password, role from member where username = ?;";
        return jdbcTemplate.query(sql, rowMapper, username)
                .stream()
                .findFirst();
    }
}
