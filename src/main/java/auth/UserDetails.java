package auth;

public class UserDetails {

    private final Long id;
    private final String password;
    private final String role;

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public UserDetails(Long id, String password, String role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }



    public boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }
}
