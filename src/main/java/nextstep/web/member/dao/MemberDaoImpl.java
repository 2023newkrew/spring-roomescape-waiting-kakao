package nextstep.web.member.dao;

import auth.login.MemberDao;
import auth.login.MemberDetail;
import nextstep.web.member.domain.Member;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Objects;

@Component
public class MemberDaoImpl implements MemberDao {
    public final JdbcTemplate jdbcTemplate;

    public MemberDaoImpl(JdbcTemplate jdbcTemplate) {
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

    @Override
    public Long save(MemberDetail member) {
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

        if (keyHolder.getKey() == null) {
            return null;
        }
        return keyHolder.getKey().longValue();
    }

    @Override
    public MemberDetail findById(Long id) {
        String sql = "SELECT id, username, password, name, phone, role from member where id = ?;";
        try {
            return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, rowMapper, id)).toMemberDetail();
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public MemberDetail findByUsername(String username) {
        String sql = "SELECT id, username, password, name, phone, role from member where username = ?;";
        try {
            return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, rowMapper, username)).toMemberDetail();
        } catch (DataAccessException e) {
            return null;
        }
    }
}
