package auth;

public class UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final String role;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public UserDetails(Long id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }



    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}
