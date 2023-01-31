package auth.login;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AbstractMember {

    private Long id;
    private String username;
    private String password;
    private String role;

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }

    public AbstractMember(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
