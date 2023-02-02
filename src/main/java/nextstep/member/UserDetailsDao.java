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

    private final RowMapper<UserDetails> rowMapper = (resultSet, rowNum) -> UserDetails.builder()
            .id(resultSet.getLong("id"))
            .username(resultSet.getString("username"))
            .password(resultSet.getString("password"))
            .name(resultSet.getString("name"))
            .phone(resultSet.getString("phone"))
            .role(resultSet.getString("role"))
            .build();

    @Override
    public UserDetails findByUsername(final String username) {
        String sql = "SELECT * from member WHERE username = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }

    @Override
    public UserDetails findById(final Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
}
