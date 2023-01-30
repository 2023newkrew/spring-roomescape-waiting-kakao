package nextstep.auth.dao;


import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.MemberRoles;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberRoleDao {
    public final JdbcTemplate jdbcTemplate;

    private final RowMapper<String> rowMapper = (resultSet, rowNum) -> resultSet.getString("role_name");

    public MemberRoles findByMemberName(final String memberName){
        final String sql = "SELECT role_name from member_role where member_name = ?;";
        List<String> memberRoles = jdbcTemplate.query(sql, rowMapper, memberName);

        return new MemberRoles(memberRoles);
    }
}
