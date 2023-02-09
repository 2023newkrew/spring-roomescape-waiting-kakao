package nextstep.member;

import auth.dao.UserDetailsDao;
import auth.domain.UserDetails;
import java.sql.PreparedStatement;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class MemberDao implements UserDetailsDao {

    private static final String SELECT_SQL = "SELECT id, username, password, name, phone, role from member ";
    public final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> rowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("phone"),
            Role.valueOf(resultSet.getString("role"))
    );

    public Long save(Member member) {
        String sql = "INSERT INTO member (username, password, name, phone, role) VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getUsername());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getName());
            ps.setString(4, member.getPhone());
            ps.setString(5, member.getRole().toString());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<Member> findById(Long id) {
        String sql = SELECT_SQL + "where id = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
    }

    public Optional<Member> findByUsername(String username) {
        String sql = SELECT_SQL + "where username = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, username));
    }

    @Override
    public Optional<UserDetails> findUserDetailsByUsername(String username) {
        return findByUsername(username).map(Member::convertToUserDetails);
    }

    @Override
    public Optional<UserDetails> findUserDetailsById(Long id) {
        return findById(id).map(Member::convertToUserDetails);
    }
}
