package auth;

public class TokenMember {
    private Long id;
    private String role;

    public TokenMember(Long id, String role) {
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
