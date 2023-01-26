package auth;

public interface UserValidator {
    UserDetails validate(String username, String password);
}
