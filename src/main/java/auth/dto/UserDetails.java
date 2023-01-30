package auth.dto;

import lombok.Getter;

@Getter
public class UserDetails {
    private Long id;
    private String role;

    public UserDetails(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
