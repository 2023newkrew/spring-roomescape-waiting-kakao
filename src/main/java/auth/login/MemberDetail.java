package auth.login;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDetail {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}
