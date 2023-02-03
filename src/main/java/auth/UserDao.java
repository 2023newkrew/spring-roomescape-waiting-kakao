package auth;

import java.util.Optional;

public interface UserDao {
    <T extends UserDetails> Long save(T userDetails);
    <T extends UserDetails> Optional<T> findById(Long id);
    <T extends UserDetails> Optional<T> findByUsername(String username);
}
