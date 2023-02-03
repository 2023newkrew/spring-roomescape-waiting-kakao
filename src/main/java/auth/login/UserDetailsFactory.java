package auth.login;

public interface UserDetailsFactory {

    UserDetails makeUserDetails(final String username, final String password);

    UserDetails convertToUserDetails(final Long id, final String role);

}
