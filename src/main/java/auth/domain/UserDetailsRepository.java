package auth.domain;

public interface UserDetailsRepository {
    UserDetails findById(Long id);

    UserDetails findByUsername(String username);
}
