package auth;

public interface UserDetailsDAO {
    UserDetails findUserById(Long id);
    UserDetails findUserByUsername(String username);
}
