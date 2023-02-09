package nextstep.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public Member(String username, String password, String name, String phone, String role) {
        this(null, username, password, name, phone, role);
    }

    public boolean checkWrongPassword(String password) {
        return !this.password.equals(password);
    }
}
