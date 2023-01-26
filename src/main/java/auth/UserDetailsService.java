package auth;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDetailsService {

    public final JdbcTemplate jdbcTemplate;

    public UserDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserDetails> rowMapper = (resultSet, rowNum) -> new UserDetails(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("phone"),
            resultSet.getString("role")
    );

    public UserDetails findById(Long id) {
        String sql = "SELECT id, username, password, name, phone, role from member where id = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public UserDetails findByUsername(String username) {
        String sql = "SELECT id, username, password, name, phone, role from member where username = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }

}
