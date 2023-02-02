package auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.member.Member;

@AllArgsConstructor
@Getter
public class UserDetail {
    private Long id;
    private String password;
    private String role;

    public UserDetail (Member member) {
        this.id = member.getId();
        this.password = member.getPassword();
        this.role = member.getRole();
    }
}
