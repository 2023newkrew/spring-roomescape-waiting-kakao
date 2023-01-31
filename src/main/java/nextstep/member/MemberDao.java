package nextstep.member;

import static nextstep.member.MemberJdbcSql.INSERT_INTO_STATEMENT;
import static nextstep.member.MemberJdbcSql.SELECT_BY_ID_STATEMENT;
import static nextstep.member.MemberJdbcSql.SELECT_BY_USERNAME_STATEMENT;

import auth.config.LoginMemberDao;
import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao implements LoginMemberDao {

    public final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Member> rowMapper = (resultSet, rowNum) -> Member.giveId(Member.builder()
            .phone(resultSet.getString("phone"))
            .name(resultSet.getString("name"))
            .role(resultSet.getString("role"))
            .username(resultSet.getString("username"))
            .password(resultSet.getString("password")).build(), resultSet.getLong("id"));

    public Long save(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_INTO_STATEMENT, new String[]{"id"});
            ps.setString(1, member.getUsername());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getName());
            ps.setString(4, member.getPhone());
            ps.setString(5, member.getRole());
            return ps;

        }, keyHolder);

        return keyHolder.getKeyAs(Long.class);
    }

    public Member findById(Long id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID_STATEMENT, rowMapper, id);
    }

    public Member findByUsername(String username) {
        return jdbcTemplate.queryForObject(SELECT_BY_USERNAME_STATEMENT, rowMapper, username);
    }
}
