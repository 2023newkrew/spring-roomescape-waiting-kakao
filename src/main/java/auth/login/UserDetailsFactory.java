package auth.login;

public interface UserDetailsFactory {
    UserDetails makeUserDetails(String username, String password);

    UserDetails convertToUserDetails(Long id, String role);

}
