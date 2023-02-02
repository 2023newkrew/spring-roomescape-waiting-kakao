package auth;

public interface UserDetails {

    Long getId();

    String getPassword();

    String getRole();

    boolean checkWrongPassword(String password);
}
