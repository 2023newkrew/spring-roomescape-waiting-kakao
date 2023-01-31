package nextstep.member.dao;

import lombok.RequiredArgsConstructor;
import nextstep.member.model.Member;
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
    public final JdbcTemplate jdbcTemplate;

    private final RowMapper<Member> rowMapper = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("member_name"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("phone")
    );

    public Long save(Member member) {
        String sql = "INSERT INTO member (member_name, password, name, phone) VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, member.getMemberName());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getName());
            ps.setString(4, member.getPhone());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, member_name, password, name, phone from member where id = ?;";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    public Optional<Member> findByMemberName(String memberName) {
        String sql = "SELECT id, member_name, password, name, phone from member where member_name = ?;";
        return jdbcTemplate.query(sql, rowMapper, memberName)
                .stream()
                .findFirst();
    }
}
