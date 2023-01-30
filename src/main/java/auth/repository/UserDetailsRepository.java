package auth.repository;

import auth.domain.persist.UserDetails;

public interface UserDetailsRepository {
    UserDetails findById(Long id);

    UserDetails findByUsername(String username);
}
