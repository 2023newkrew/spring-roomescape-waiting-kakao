package auth;

public interface UserDetails {
    Long getId();
    String getUsername();
    String getPassword();
    String getName();
    String getPhone();
    String getRole();
    boolean checkPassword(String password);
}
