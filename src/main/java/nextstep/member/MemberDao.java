package nextstep.member;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Member> rowMapper = (resultSet, rowNum) -> Member.builder()
            .id(resultSet.getLong("id"))
            .username(resultSet.getString("username"))
            .password(resultSet.getString("password"))
            .name(resultSet.getString("name"))
            .phone(resultSet.getString("phone"))
            .role(Role.valueOf(resultSet.getString("role")))
            .build();

    public Long save(Member member) {
        String sql = "INSERT INTO member (username, password, name, phone, role) VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getUsername());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getName());
            ps.setString(4, member.getPhone());
            ps.setString(5, member.getRole().name());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, username, password, name, phone, role from member where id = ?;";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findAny();
    }

    public Optional<Member> findByUsername(String username) {
        String sql = "SELECT id, username, password, name, phone, role from member where username = ?;";
        return jdbcTemplate.query(sql, rowMapper, username)
                .stream()
                .findAny();
    }
}
