package auth.dao;

import auth.domain.UserDetails;
import java.util.Optional;

public interface UserDetailsDao {
    Optional<UserDetails> findUserDetailsByUsername(String username);

    Optional<UserDetails> findUserDetailsById(Long id);
}
