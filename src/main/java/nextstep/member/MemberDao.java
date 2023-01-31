package nextstep.member;

import auth.AuthDao;
import auth.UserDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Optional;

@Component
public class MemberDao implements AuthDao {
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
            resultSet.getString("role")
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
            ps.setString(5, member.getRole());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Member findById(Long id) {
        String sql = "SELECT id, username, password, name, phone, role from member where id = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Member findByUsername(String username) {
        String sql = "SELECT id, username, password, name, phone, role from member where username = ?;";
        return jdbcTemplate.queryForObject(sql, rowMapper, username);
    }

    @Override
    public Optional<UserDetails> findUserDetailsByUserName(String userName) {
        Member member = findByUsername(userName);

        if (member == null) {
            return Optional.empty();
        }

        UserDetails userDetails = new UserDetails(
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getName(),
                member.getPhone(),
                member.getRole()
        );

        return Optional.of(userDetails);
    }

    @Override
    public Optional<UserDetails> findUserDetailsById(Long id) {
        Member member = findById(id);

        if (member == null) {
            return Optional.empty();
        }

        UserDetails userDetails = new UserDetails(
                member.getId(),
                member.getUsername(),
                member.getPassword(),
                member.getName(),
                member.getPhone(),
                member.getRole()
        );

        return Optional.of(userDetails);
    }
}
