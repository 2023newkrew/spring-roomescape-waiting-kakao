package auth;

public interface UserDetailsDao {
    UserDetails findUserById(Long id);
    UserDetails findUserByUsername(String username);
}
