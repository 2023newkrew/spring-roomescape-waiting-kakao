package auth;

public interface UserDao {
    <T extends UserDetails> Long save(T userDetails);
    UserDetails findById(Long id);
    UserDetails findByUsername(String username);
}
