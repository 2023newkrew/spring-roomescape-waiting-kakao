package auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class UserDetails {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public boolean checkWrongPassword(String password) {
        return !Objects.equals(this.password, password);
    }
}
