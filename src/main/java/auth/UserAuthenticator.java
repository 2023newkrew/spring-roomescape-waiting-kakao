package auth;

public interface UserAuthenticator {
    UserDetails authenticate(String username, String password);
}
