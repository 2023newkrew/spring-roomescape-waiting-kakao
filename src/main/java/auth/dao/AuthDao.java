package auth.dao;


import auth.domain.RoleType;
import auth.domain.RoleTypes;
import auth.model.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthDao {
    public final JdbcTemplate jdbcTemplate;

    private final RowMapper<String> memberRoleRowMapper = (resultSet, rowNum) -> resultSet.getString("role_name");

    private final RowMapper<MemberDetails> memberDetailsRowMapper = (resultSet, rowNum) -> new MemberDetails(
            resultSet.getLong("id"),
            resultSet.getString("member_name"),
            resultSet.getString("password")
    );

    public RoleTypes findMemberRolesByMemberName(final String memberName){
        final String sql = "SELECT role_name from member_role where member_name = ?;";
        List<String> memberRoles = jdbcTemplate.query(sql, memberRoleRowMapper, memberName);

        return new RoleTypes(RoleType.of(memberRoles));
    }

    public Optional<MemberDetails> findMemberDetailsByMemberName(String memberName) {
        String sql = "SELECT id, member_name, password phone from member where member_name = ?;";
        return jdbcTemplate.query(sql, memberDetailsRowMapper, memberName)
                .stream()
                .findFirst();
    }
}
