package auth;

public class LoginRequestEvent {
    private Long id;
    private final String username;
    private final String password;
    private String role;

    public LoginRequestEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setMemberInformation(Long id, String role) {
        this.id = id;
        this.role = role;
    }

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
}
