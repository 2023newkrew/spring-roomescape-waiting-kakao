package auth.dto;

public class UserDetails {
    private Long id;
    private String role;

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
