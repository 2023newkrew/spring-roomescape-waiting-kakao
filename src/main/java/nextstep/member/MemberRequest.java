package nextstep.member;

import auth.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class MemberRequest {
    private String username;
    private String password;
    private String name;
    private String phone;
    private Role role;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .name(name)
                .phone(phone)
                .role(role)
                .build();
    }
}
