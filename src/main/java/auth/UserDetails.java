package auth;

import auth.exception.NullFieldException;

public class UserDetails {
    private final Long id;
    private final String role;

    public UserDetails(Long id, String role) {
        if (id == null || role == null){
            throw new NullFieldException();
        }
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