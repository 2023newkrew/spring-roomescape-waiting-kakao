package roomwaiting.auth.userdetail;


public interface UserDetailsService {
    UserDetails findUserDetailsByUsername(String username);

    UserDetails findUserDetailsById(Long id);
}
