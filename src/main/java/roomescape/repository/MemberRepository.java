package roomescape.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Member;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private static final RowMapper<Member> ROW_MAPPER = (resultSet, rowNum) -> new Member(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("phone"),
            resultSet.getBoolean("is_admin")
    );
    private final NamedParameterJdbcTemplate jdbc;

    public Long insert(String username, String password, String name, String phone) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into member (username, password, name, phone) values (:username, :password, :name, :phone)", new MapSqlParameterSource(Map.of(
                "username", username,
                "password", password,
                "name", name,
                "phone", phone
        )), keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Member selectById(Long id) {
        return jdbc.queryForObject("select id, username, password, name, phone, is_admin from member where id = :id", Map.of("id", id), ROW_MAPPER);
    }

    public Optional<Member> selectByUsername(String username) {
        return Optional.ofNullable(jdbc.queryForObject("select id, username, password, name, phone, is_admin from member where username = :username", Map.of("username", username), ROW_MAPPER));
    }
}
