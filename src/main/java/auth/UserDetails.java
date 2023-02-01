package auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserDetails {
    private final Long id;
    private final String password;
    private final String role;

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}
