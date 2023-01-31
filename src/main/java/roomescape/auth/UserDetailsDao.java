package roomescape.auth;

public interface UserDetailsDao {
    UserDetails findByUsername(String username);
}
