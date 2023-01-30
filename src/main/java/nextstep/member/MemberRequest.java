package nextstep.member;

import auth.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberRequest {
    private String username;
    private String password;
    private String name;
    private String phone;
    private Role role;

    public Member toEntity() {
        return new Member(username, password, name, phone, role);
    }
}
