package auth.domain;

public interface UserDetails {

    String getUsername();

    String getPassword();

    UserRole getRole();

    boolean isWrongPassword(String password);

    boolean isNotAdmin();
}
