package auth;

public interface AuthenticationProvider {
    UserDetails findById(Long id);
    UserDetails findByUsername(String username);
}
