package auth.service;

import auth.dto.MemberDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberDetailsService {

    private final JdbcTemplate jdbcTemplate;

    public MemberDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MemberDetails> loadMemberDetailsByUsername(String username) {
        String sql = "SELECT id, username, password, name, phone, role from member where username = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, username));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    private final RowMapper<MemberDetails> rowMapper = (resultSet, rowNum) -> new MemberDetails(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("phone"),
            resultSet.getString("role")
    );
}
