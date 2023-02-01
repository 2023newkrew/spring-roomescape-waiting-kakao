package auth;

public interface UserDetailsService {

    UserDetails findById(Long id);

    UserDetails findByUsername(String username);
}
