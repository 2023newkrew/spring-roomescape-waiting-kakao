package auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDetails {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public boolean isValidPassword(String password) {
        return this.password.equals(password);
    }

}
