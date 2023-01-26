package auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.member.Member;

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

    public Member toMember() {
        return new Member(id, username, password, name, phone, role);
    }

}
