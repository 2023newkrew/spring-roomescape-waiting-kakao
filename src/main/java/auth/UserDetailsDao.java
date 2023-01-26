package auth;

public interface UserDetailsDao {
    UserDetails findUserDetailsByUsername(String username);

    UserDetails findUserDetailsById(Long id);
}
