package auth;

public class UserDetails {
    private final Long id;
    private final String role;

    public UserDetails(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}