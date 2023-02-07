package auth.dao;

import auth.domain.UserDetails;

public interface UserDetailsDao {
    UserDetails findUserDetailsByUsername(String username);

    UserDetails findUserDetailsById(Long id);
}
