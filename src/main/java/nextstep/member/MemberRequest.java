package nextstep.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberRequest {
    private final String username;
    private final String password;
    private final String name;
    private final String phone;
    private final String role;

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
