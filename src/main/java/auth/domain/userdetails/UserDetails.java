package auth.domain.userdetails;

public class UserDetails {

    private Long id;
    private String password;
    private String role;

    public UserDetails(Long id, String password, String role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}
