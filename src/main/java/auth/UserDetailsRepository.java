package auth;

public interface UserDetailsRepository {
    UserDetails findById(Long id);

    UserDetails findByUsername(String username);
}
