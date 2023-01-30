package auth;

public interface UserDetails {

    String getUsername();

    String getRole();

    boolean checkWrongPassword(String password);
}
