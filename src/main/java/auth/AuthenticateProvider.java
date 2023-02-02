package auth;

public interface AuthenticateProvider {
    UserDetails createUserDetails(String username, String password);

    UserDetails createUserDetails(Long principal, String role);

}
