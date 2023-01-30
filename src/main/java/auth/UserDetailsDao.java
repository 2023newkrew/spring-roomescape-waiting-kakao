package auth;

public interface UserDetailsDao {
    UserDetails findByUsername2(String username);

    UserDetails findById(Long id);
}
