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

    public MemberRequest(String username, String password, String name, String phone) {
        this(username, password, name, phone, null);
    }

    public Member toEntity() {
        Member.MemberBuilder memberBuilder = Member.builder()
                .username(username)
                .password(password)
                .name(name)
                .phone(phone);

        if (role != null) {
            memberBuilder.role(Role.valueOf(role));
        }

        return memberBuilder.build();
    }
}
