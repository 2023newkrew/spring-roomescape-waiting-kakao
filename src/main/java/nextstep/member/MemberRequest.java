package nextstep.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberRequest {
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;


    public Member toEntity() {
        return new Member(username, password, name, phone, role);
    }
}
