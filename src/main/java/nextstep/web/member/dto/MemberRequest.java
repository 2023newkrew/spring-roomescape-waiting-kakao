package nextstep.web.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.web.member.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
