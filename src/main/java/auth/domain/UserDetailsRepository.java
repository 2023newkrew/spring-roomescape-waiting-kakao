package auth.domain;

import java.util.Optional;

public interface UserDetailsRepository {

    Optional<UserDetails> findByUsername(String username);
}
