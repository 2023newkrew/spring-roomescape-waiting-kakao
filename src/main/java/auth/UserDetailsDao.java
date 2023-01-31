package auth;

public interface UserDetailsDao {
    UserDetails findUserDetailsById(Long id);
    UserDetails findUserDetailsByUsername(String username);
}
