package nextstep.member;

import auth.MemberDetails;
import auth.MemberDetailsService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class MemberDetailsServiceImpl implements MemberDetailsService {

    private final JdbcTemplate jdbcTemplate;

    public MemberDetailsServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberDetails> rowMapper = (resultSet, rowNum) -> new MemberDetails(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("phone"),
            resultSet.getString("role")
    );

    public MemberDetails loadMemberDetailsByUsername(String username) {
        String sql = "SELECT id, username, password, name, phone, role from member where username = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }
}
