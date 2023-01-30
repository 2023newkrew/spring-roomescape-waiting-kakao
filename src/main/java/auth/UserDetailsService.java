package auth;

public interface UserDetailsService<T extends UserDetails<U>, U> {

    T findByUsername(String username);

    T findById(U id);
}
