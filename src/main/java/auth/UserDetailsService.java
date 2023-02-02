package auth;

public interface UserDetailsService {

    UserDetails findByUsername(String username);

    UserDetails findById(Long id);
}
