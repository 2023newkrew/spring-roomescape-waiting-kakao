package auth.repository;

import auth.domain.persist.UserDetails;
import nextstep.domain.persist.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

public interface UserDetailsRepository {
    UserDetails findById(Long id);
    UserDetails findByUsername(String username);
}
