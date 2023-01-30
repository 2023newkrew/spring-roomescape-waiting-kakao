package auth;

public interface UserDetailsFactory {
    UserDetails createUserDetails(String username, String password);

    UserDetails createUserDetails(Long principal, String role);

}
