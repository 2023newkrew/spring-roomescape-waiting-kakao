package roomwaiting.nextstep.member;

import roomwaiting.nextstep.dbmapper.DatabaseMapper;
import roomwaiting.nextstep.dbmapper.H2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Optional;

@Component
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final DatabaseMapper databaseMapper;
    @Autowired
    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.databaseMapper = new H2Mapper();
    }

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
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<Member> findByUsername(String username) {
        String sql = "SELECT id, username, password, name, phone, role from member where username = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, databaseMapper.memberRowMapper(), username));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateAdmin(String username) {
        String sql = "UPDATE member SET ROLE='ADMIN' where username = ?;";
        jdbcTemplate.update(sql, username);
    }
    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, username, password, name, phone, role from member where id = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, databaseMapper.memberRowMapper(), id));
    }
}
