package auth;

public interface UserAuthenticator {
    UserDetails authenticate(String username, String password);

    String getRole(Long id);
}
