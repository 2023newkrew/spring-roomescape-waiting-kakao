package auth;

import java.util.Optional;

public interface AuthDao {
    Optional<UserDetails> findUserDetailsByUserName(String userName);
    Optional<UserDetails> findUserDetailsById(Long id);
}
