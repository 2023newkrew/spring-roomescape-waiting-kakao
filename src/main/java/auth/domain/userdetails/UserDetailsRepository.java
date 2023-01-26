package auth.domain.userdetails;

public interface UserDetailsRepository {
    UserDetails findById(Long id);

    UserDetails findByUsername(String username);
}
