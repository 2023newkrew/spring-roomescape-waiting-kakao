package auth;

public interface UserDetails {

    String getUsername();

    String getPassword();

    String getRole();

    boolean checkWrongPassword(String password);
}
