package nextstep.member;

import auth.UserDetails;
import auth.UserDetailsRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserDetailsDao implements UserDetailsRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserDetailsDao(final JdbcTemplate jdbcTemplate) {
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

    @Override
    public UserDetails findByUsername(final String username) {
        String sql = "SELECT id, username, password, name, phone, role from member where username = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }
}
